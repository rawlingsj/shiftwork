package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.PlanningJob;
import com.teammachine.staffrostering.domain.enumeration.JobStatus;
import com.teammachine.staffrostering.repository.PlanningJobRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
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

    private static final String DEFAULT_JOB_ID = "AAAAA";
    private static final String UPDATED_JOB_ID = "BBBBB";

    private static final JobStatus DEFAULT_STATUS = JobStatus.PENDING;
    private static final JobStatus UPDATED_STATUS = JobStatus.RUNNING;

    @Inject
    private PlanningJobRepository planningJobRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPlanningJobMockMvc;

    private PlanningJob planningJob;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlanningJobResource planningJobResource = new PlanningJobResource();
        ReflectionTestUtils.setField(planningJobResource, "planningJobRepository", planningJobRepository);
        this.restPlanningJobMockMvc = MockMvcBuilders.standaloneSetup(planningJobResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        planningJob = new PlanningJob();
        planningJob.setJobId(DEFAULT_JOB_ID);
        planningJob.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createPlanningJob() throws Exception {
        int databaseSizeBeforeCreate = planningJobRepository.findAll().size();

        // Create the PlanningJob

        restPlanningJobMockMvc.perform(post("/api/planning-jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(planningJob)))
                .andExpect(status().isCreated());

        // Validate the PlanningJob in the database
        List<PlanningJob> planningJobs = planningJobRepository.findAll();
        assertThat(planningJobs).hasSize(databaseSizeBeforeCreate + 1);
        PlanningJob testPlanningJob = planningJobs.get(planningJobs.size() - 1);
        assertThat(testPlanningJob.getJobId()).isEqualTo(DEFAULT_JOB_ID);
        assertThat(testPlanningJob.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void getAllPlanningJobs() throws Exception {
        // Initialize the database
        planningJobRepository.saveAndFlush(planningJob);

        // Get all the planningJobs
        restPlanningJobMockMvc.perform(get("/api/planning-jobs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(planningJob.getId().intValue())))
                .andExpect(jsonPath("$.[*].jobId").value(hasItem(DEFAULT_JOB_ID.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getPlanningJob() throws Exception {
        // Initialize the database
        planningJobRepository.saveAndFlush(planningJob);

        // Get the planningJob
        restPlanningJobMockMvc.perform(get("/api/planning-jobs/{id}", planningJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(planningJob.getId().intValue()))
            .andExpect(jsonPath("$.jobId").value(DEFAULT_JOB_ID.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlanningJob() throws Exception {
        // Get the planningJob
        restPlanningJobMockMvc.perform(get("/api/planning-jobs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanningJob() throws Exception {
        // Initialize the database
        planningJobRepository.saveAndFlush(planningJob);
        int databaseSizeBeforeUpdate = planningJobRepository.findAll().size();

        // Update the planningJob
        PlanningJob updatedPlanningJob = new PlanningJob();
        updatedPlanningJob.setId(planningJob.getId());
        updatedPlanningJob.setJobId(UPDATED_JOB_ID);
        updatedPlanningJob.setStatus(UPDATED_STATUS);

        restPlanningJobMockMvc.perform(put("/api/planning-jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPlanningJob)))
                .andExpect(status().isOk());

        // Validate the PlanningJob in the database
        List<PlanningJob> planningJobs = planningJobRepository.findAll();
        assertThat(planningJobs).hasSize(databaseSizeBeforeUpdate);
        PlanningJob testPlanningJob = planningJobs.get(planningJobs.size() - 1);
        assertThat(testPlanningJob.getJobId()).isEqualTo(UPDATED_JOB_ID);
        assertThat(testPlanningJob.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deletePlanningJob() throws Exception {
        // Initialize the database
        planningJobRepository.saveAndFlush(planningJob);
        int databaseSizeBeforeDelete = planningJobRepository.findAll().size();

        // Get the planningJob
        restPlanningJobMockMvc.perform(delete("/api/planning-jobs/{id}", planningJob.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PlanningJob> planningJobs = planningJobRepository.findAll();
        assertThat(planningJobs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
