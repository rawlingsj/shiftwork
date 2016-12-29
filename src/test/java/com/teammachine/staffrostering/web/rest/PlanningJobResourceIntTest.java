package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.PlanningJob;
import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.domain.enumeration.JobStatus;
import com.teammachine.staffrostering.planner.PlannerEngine;
import com.teammachine.staffrostering.planner.PlannerEngineJob;
import com.teammachine.staffrostering.planner.PlannerEngineJobResult;
import com.teammachine.staffrostering.repository.PlanningJobRepository;
import com.teammachine.staffrostering.repository.StaffRosterParametrizationRepository;
import com.teammachine.staffrostering.service.PlanningJobService;
import com.teammachine.staffrostering.service.impl.PlanningJobServiceImpl;
import com.teammachine.staffrostering.web.rest.errors.ErrorConstants;
import com.teammachine.staffrostering.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PlanningJobResource REST controller.
 *
 * @see PlanningJobResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class PlanningJobResourceIntTest {

    private static final String JOB_ID = "xxxxx";
    private static final JobStatus DEFAULT_STATUS = JobStatus.RUNNING;
    private static final JobStatus UPDATED_STATUS = JobStatus.COMPLETED;

    @Mock
    private PlannerEngine plannerEngine;
    @Inject
    private PlanningJobRepository planningJobRepository;
    @Inject
    private StaffRosterParametrizationRepository staffRosterParametrizationRepository;
    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    @Inject
    private ExceptionTranslator exceptionTranslator;

    private PlanningJobService planningJobService;

    private MockMvc restPlanningJobMockMvc;

    private PlanningJob planningJob;

    private StaffRosterParametrization staffRosterParametrization;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.planningJobService = new PlanningJobServiceImpl();
        ReflectionTestUtils.setField(planningJobService, "plannerEngine", plannerEngine);
        ReflectionTestUtils.setField(planningJobService, "planningJobRepository", planningJobRepository);
        PlanningJobResource planningJobResource = new PlanningJobResource();
        ReflectionTestUtils.setField(planningJobResource, "planningJobService", planningJobService);
        this.restPlanningJobMockMvc = MockMvcBuilders.standaloneSetup(planningJobResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter)
            .setControllerAdvice(exceptionTranslator)
            .build();
    }

    @Before
    public void init() {
        staffRosterParametrization = new StaffRosterParametrization();
        staffRosterParametrizationRepository.save(staffRosterParametrization);

        planningJob = new PlanningJob();
        planningJob.setParameterization(staffRosterParametrization);
        planningJob.setJobId(JOB_ID);
        planningJob.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createPlanningJob() throws Exception {
        PlannerEngineJob plannerEngineJob = mock(PlannerEngineJob.class);
        when(plannerEngineJob.getJobId()).thenReturn(JOB_ID);
        when(plannerEngineJob.getStatus()).thenReturn(DEFAULT_STATUS);
        when(plannerEngine.runPlanningJob(Matchers.eq(staffRosterParametrization))).thenReturn(Optional.of(plannerEngineJob));

        PlanningJob planningJob = new PlanningJob();
        planningJob.setParameterization(staffRosterParametrization);

        // Business method
        restPlanningJobMockMvc.perform(post("/api/planning-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningJob)))
            .andExpect(status().isCreated());

        // Asserts
        List<PlanningJob> planningJobs = planningJobRepository.findAll();
        assertThat(planningJobs).hasSize(1);
        PlanningJob testPlanningJob = planningJobs.get(0);
        assertThat(testPlanningJob.getId()).isNotNull();
        assertThat(testPlanningJob.getJobId()).isEqualTo(JOB_ID);
        assertThat(testPlanningJob.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPlanningJob.getParameterization()).isEqualTo(staffRosterParametrization);
    }

    @Test
    @Transactional
    public void unableToRunPlanningJob() throws Exception {
        when(plannerEngine.runPlanningJob(Matchers.eq(staffRosterParametrization))).thenReturn(Optional.empty());

        // Business method
        restPlanningJobMockMvc.perform(post("/api/planning-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningJob)))
            // Asserts
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorConstants.ERR_UNABLE_TO_RUN_PLANNING_JOB))
            .andExpect(jsonPath("$.params[*]").value(empty()));
    }

    @Test
    @Transactional
    public void getAllPlanningJobs() throws Exception {
        planningJobRepository.saveAndFlush(planningJob);

        // Business method
        restPlanningJobMockMvc.perform(get("/api/planning-jobs"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Asserts
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobId").value(hasItem(JOB_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getPlanningJobWithResult() throws Exception {
        planningJobRepository.saveAndFlush(planningJob);

        PlannerEngineJob plannerEngineJob = mock(PlannerEngineJob.class);
        PlannerEngineJobResult plannerEngineJobResult = mock(PlannerEngineJobResult.class);
        when(plannerEngineJobResult.getHardConstraintMatches()).thenReturn(-101);
        when(plannerEngineJobResult.getSoftConstraintMatches()).thenReturn(-102);
        List<ShiftAssignment> shiftAssignments = Arrays.asList(
            mockShiftAssignment(1),
            mockShiftAssignment(2)
        );
        when(plannerEngineJobResult.getShiftAssignments()).thenReturn(shiftAssignments);
        when(plannerEngineJob.getResult()).thenReturn(plannerEngineJobResult);
        when(plannerEngine.getPlanningJob(JOB_ID)).thenReturn(Optional.of(plannerEngineJob));

        // Business method
        restPlanningJobMockMvc.perform(get("/api/planning-jobs/{id}", planningJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Asserts
            .andExpect(jsonPath("$.id").value(planningJob.getId().intValue()))
            .andExpect(jsonPath("$.jobId").value(JOB_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.parameterization.id").value(staffRosterParametrization.getId().intValue()))
            .andExpect(jsonPath("$.shiftAssignments[*].id").value(contains(1, 2)));
    }

    private ShiftAssignment mockShiftAssignment(int id) {
        ShiftAssignment shiftAssignment = new ShiftAssignment();
        shiftAssignment.setId(Integer.valueOf(id).longValue());
        return shiftAssignment;
    }

    @Test
    @Transactional
    public void getNonExistingPlanningJob() throws Exception {
        int id = 1001;
        // Business method
        restPlanningJobMockMvc.perform(get("/api/planning-jobs/{id}", id))
            // Asserts
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanningJob() throws Exception {
        planningJobRepository.saveAndFlush(planningJob);

        PlanningJob updatedPlanningJob = new PlanningJob();
        updatedPlanningJob.setJobId(JOB_ID);
        updatedPlanningJob.setStatus(UPDATED_STATUS);

        // Business method
        restPlanningJobMockMvc.perform(put("/api/planning-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlanningJob)))
            .andExpect(status().isOk());

        // Asserts
        List<PlanningJob> planningJobs = planningJobRepository.findAll();
        assertThat(planningJobs).hasSize(1);
        PlanningJob testPlanningJob = planningJobs.get(0);
        assertThat(testPlanningJob.getJobId()).isEqualTo(JOB_ID);
        assertThat(testPlanningJob.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void syncAllPlanningJobs() throws Exception {
        planningJobRepository.saveAndFlush(planningJob);

        PlannerEngineJob plannerEngineJob = mock(PlannerEngineJob.class);
        when(plannerEngineJob.getJobId()).thenReturn(JOB_ID);
        when(plannerEngineJob.getStatus()).thenReturn(UPDATED_STATUS);
        when(plannerEngine.getAllPlanningJobs()).thenReturn(Collections.singletonList(plannerEngineJob));

        // Business method
        restPlanningJobMockMvc.perform(put("/api/planning-jobs"))
            .andExpect(status().isOk());

        // Asserts
        List<PlanningJob> planningJobs = planningJobRepository.findAll();
        assertThat(planningJobs).hasSize(1);
        PlanningJob testPlanningJob = planningJobs.get(0);
        assertThat(testPlanningJob.getId()).isEqualTo(planningJob.getId());
        assertThat(testPlanningJob.getJobId()).isEqualTo(JOB_ID);
        assertThat(testPlanningJob.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void syncPlanningJobStatus() throws Exception {
        planningJobRepository.saveAndFlush(planningJob);

        PlannerEngineJob plannerEngineJob = mock(PlannerEngineJob.class);
        when(plannerEngineJob.getJobId()).thenReturn(JOB_ID);
        when(plannerEngineJob.getStatus()).thenReturn(UPDATED_STATUS);
        when(plannerEngine.getPlanningJob(JOB_ID)).thenReturn(Optional.of(plannerEngineJob));

        // Business method
        restPlanningJobMockMvc.perform(put("/api/planning-jobs/{id}", planningJob.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            // Asserts
            .andExpect(jsonPath("$.jobId").value(JOB_ID))
            .andExpect(jsonPath("$.status").value(UPDATED_STATUS.toString()));

        List<PlanningJob> planningJobs = planningJobRepository.findAll();
        assertThat(planningJobs).hasSize(1);
        PlanningJob testPlanningJob = planningJobs.get(0);
        assertThat(testPlanningJob.getJobId()).isEqualTo(JOB_ID);
        assertThat(testPlanningJob.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void syncPlanningJobStatusOfNonExistingPlanningJob() throws Exception {
        int id = 1001;
        // Business method
        restPlanningJobMockMvc.perform(put("/api/planning-jobs/{id}", id))
            // Asserts
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void deletePlanningJob() throws Exception {
        planningJobRepository.saveAndFlush(planningJob);

        // Business method
        restPlanningJobMockMvc.perform(delete("/api/planning-jobs/{id}", planningJob.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Asserts
        verify(plannerEngine).terminateAndDeleteJob(JOB_ID);
        List<PlanningJob> planningJobs = planningJobRepository.findAll();
        assertThat(planningJobs).isEmpty();
    }

    @Test
    @Transactional
    public void deleteNonExistingPlanningJob() throws Exception {
        int id = 1001;
        // Business method
        restPlanningJobMockMvc.perform(delete("/api/planning-jobs/{id}", id))
            // Asserts
            .andExpect(status().isNotFound());
    }
}
