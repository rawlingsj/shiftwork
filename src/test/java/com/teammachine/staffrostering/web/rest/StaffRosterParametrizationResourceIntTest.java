package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.PlanningJob;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.repository.StaffRosterParametrizationRepository;
import com.teammachine.staffrostering.service.PlanningJobService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StaffRosterParametrizationResource REST controller.
 *
 * @see StaffRosterParametrizationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class StaffRosterParametrizationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_HARD_CONSTRAINT_MATCHES = 1;
    private static final Integer UPDATED_HARD_CONSTRAINT_MATCHES = 2;

    private static final Integer DEFAULT_SOFT_CONSTRAINT_MATCHES = 1;
    private static final Integer UPDATED_SOFT_CONSTRAINT_MATCHES = 2;

    private static final ZonedDateTime DEFAULT_LAST_RUN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_RUN_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_RUN_TIME_STR = dateTimeFormatter.format(DEFAULT_LAST_RUN_TIME);

    @Inject
    private StaffRosterParametrizationRepository staffRosterParametrizationRepository;

    @Mock
    private PlanningJobService planningJobService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStaffRosterParametrizationMockMvc;

    private StaffRosterParametrization staffRosterParametrization;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StaffRosterParametrizationResource staffRosterParametrizationResource = new StaffRosterParametrizationResource();
        ReflectionTestUtils.setField(staffRosterParametrizationResource, "staffRosterParametrizationRepository", staffRosterParametrizationRepository);
        ReflectionTestUtils.setField(staffRosterParametrizationResource, "planningJobService", planningJobService);
        this.restStaffRosterParametrizationMockMvc = MockMvcBuilders.standaloneSetup(staffRosterParametrizationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        staffRosterParametrization = new StaffRosterParametrization();
        staffRosterParametrization.setName(DEFAULT_NAME);
        staffRosterParametrization.setDescription(DEFAULT_DESCRIPTION);
        staffRosterParametrization.setHardConstraintMatches(DEFAULT_HARD_CONSTRAINT_MATCHES);
        staffRosterParametrization.setSoftConstraintMatches(DEFAULT_SOFT_CONSTRAINT_MATCHES);
        staffRosterParametrization.setLastRunTime(DEFAULT_LAST_RUN_TIME);
    }


    @Test
    @Transactional
    public void createStaffRosterParametrization() throws Exception {
        int databaseSizeBeforeCreate = staffRosterParametrizationRepository.findAll().size();
        PlanningJob panningJob = mock(PlanningJob.class);
        when(planningJobService.runPlanningJob(anyObject())).thenReturn(Optional.of(panningJob));

        // Create the StaffRosterParametrization
        restStaffRosterParametrizationMockMvc.perform(post("/api/staff-roster-parametrizations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(staffRosterParametrization)))
                .andExpect(status().isCreated());

        // Validate the StaffRosterParametrization in the database
        List<StaffRosterParametrization> staffRosterParametrizations = staffRosterParametrizationRepository.findAll();
        assertThat(staffRosterParametrizations).hasSize(databaseSizeBeforeCreate + 1);
        StaffRosterParametrization testStaffRosterParametrization = staffRosterParametrizations.get(staffRosterParametrizations.size() - 1);
        assertThat(testStaffRosterParametrization.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStaffRosterParametrization.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStaffRosterParametrization.getHardConstraintMatches()).isEqualTo(DEFAULT_HARD_CONSTRAINT_MATCHES);
        assertThat(testStaffRosterParametrization.getSoftConstraintMatches()).isEqualTo(DEFAULT_SOFT_CONSTRAINT_MATCHES);
        assertThat(testStaffRosterParametrization.getLastRunTime()).isEqualTo(DEFAULT_LAST_RUN_TIME);
    }

    @Test
    @Transactional
    public void getAllStaffRosterParametrizations() throws Exception {
        // Initialize the database
        staffRosterParametrizationRepository.saveAndFlush(staffRosterParametrization);

        // Get all the staffRosterParametrizations
        restStaffRosterParametrizationMockMvc.perform(get("/api/staff-roster-parametrizations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(staffRosterParametrization.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].hardConstraintMatches").value(hasItem(DEFAULT_HARD_CONSTRAINT_MATCHES)))
                .andExpect(jsonPath("$.[*].softConstraintMatches").value(hasItem(DEFAULT_SOFT_CONSTRAINT_MATCHES)))
                .andExpect(jsonPath("$.[*].lastRunTime").value(hasItem(DEFAULT_LAST_RUN_TIME_STR)));
    }

    @Test
    @Transactional
    public void getStaffRosterParametrization() throws Exception {
        // Initialize the database
        staffRosterParametrizationRepository.saveAndFlush(staffRosterParametrization);

        // Get the staffRosterParametrization
        restStaffRosterParametrizationMockMvc.perform(get("/api/staff-roster-parametrizations/{id}", staffRosterParametrization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(staffRosterParametrization.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.hardConstraintMatches").value(DEFAULT_HARD_CONSTRAINT_MATCHES))
            .andExpect(jsonPath("$.softConstraintMatches").value(DEFAULT_SOFT_CONSTRAINT_MATCHES))
            .andExpect(jsonPath("$.lastRunTime").value(DEFAULT_LAST_RUN_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingStaffRosterParametrization() throws Exception {
        // Get the staffRosterParametrization
        restStaffRosterParametrizationMockMvc.perform(get("/api/staff-roster-parametrizations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStaffRosterParametrization() throws Exception {
        // Initialize the database
        staffRosterParametrizationRepository.saveAndFlush(staffRosterParametrization);
        int databaseSizeBeforeUpdate = staffRosterParametrizationRepository.findAll().size();

        // Update the staffRosterParametrization
        StaffRosterParametrization updatedStaffRosterParametrization = new StaffRosterParametrization();
        updatedStaffRosterParametrization.setId(staffRosterParametrization.getId());
        updatedStaffRosterParametrization.setName(UPDATED_NAME);
        updatedStaffRosterParametrization.setDescription(UPDATED_DESCRIPTION);
        updatedStaffRosterParametrization.setHardConstraintMatches(UPDATED_HARD_CONSTRAINT_MATCHES);
        updatedStaffRosterParametrization.setSoftConstraintMatches(UPDATED_SOFT_CONSTRAINT_MATCHES);
        updatedStaffRosterParametrization.setLastRunTime(UPDATED_LAST_RUN_TIME);

        restStaffRosterParametrizationMockMvc.perform(put("/api/staff-roster-parametrizations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStaffRosterParametrization)))
                .andExpect(status().isOk());

        // Validate the StaffRosterParametrization in the database
        List<StaffRosterParametrization> staffRosterParametrizations = staffRosterParametrizationRepository.findAll();
        assertThat(staffRosterParametrizations).hasSize(databaseSizeBeforeUpdate);
        StaffRosterParametrization testStaffRosterParametrization = staffRosterParametrizations.get(staffRosterParametrizations.size() - 1);
        assertThat(testStaffRosterParametrization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStaffRosterParametrization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStaffRosterParametrization.getHardConstraintMatches()).isEqualTo(UPDATED_HARD_CONSTRAINT_MATCHES);
        assertThat(testStaffRosterParametrization.getSoftConstraintMatches()).isEqualTo(UPDATED_SOFT_CONSTRAINT_MATCHES);
        assertThat(testStaffRosterParametrization.getLastRunTime()).isEqualTo(UPDATED_LAST_RUN_TIME);
    }

    @Test
    @Transactional
    public void deleteStaffRosterParametrization() throws Exception {
        // Initialize the database
        staffRosterParametrizationRepository.saveAndFlush(staffRosterParametrization);
        int databaseSizeBeforeDelete = staffRosterParametrizationRepository.findAll().size();

        // Get the staffRosterParametrization
        restStaffRosterParametrizationMockMvc.perform(delete("/api/staff-roster-parametrizations/{id}", staffRosterParametrization.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StaffRosterParametrization> staffRosterParametrizations = staffRosterParametrizationRepository.findAll();
        assertThat(staffRosterParametrizations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
