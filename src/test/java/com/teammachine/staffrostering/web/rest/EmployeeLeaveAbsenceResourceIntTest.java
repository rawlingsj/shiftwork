package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.EmployeeLeaveAbsence;
import com.teammachine.staffrostering.repository.EmployeeLeaveAbsenceRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EmployeeLeaveAbsenceResource REST controller.
 *
 * @see EmployeeLeaveAbsenceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class EmployeeLeaveAbsenceResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_ABSENT_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_ABSENT_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_ABSENT_FROM_STR = dateTimeFormatter.format(DEFAULT_ABSENT_FROM);

    private static final ZonedDateTime DEFAULT_ABSENT_TO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_ABSENT_TO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_ABSENT_TO_STR = dateTimeFormatter.format(DEFAULT_ABSENT_TO);

    @Inject
    private EmployeeLeaveAbsenceRepository employeeLeaveAbsenceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeeLeaveAbsenceMockMvc;

    private EmployeeLeaveAbsence employeeLeaveAbsence;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeLeaveAbsenceResource employeeLeaveAbsenceResource = new EmployeeLeaveAbsenceResource();
        ReflectionTestUtils.setField(employeeLeaveAbsenceResource, "employeeLeaveAbsenceRepository", employeeLeaveAbsenceRepository);
        this.restEmployeeLeaveAbsenceMockMvc = MockMvcBuilders.standaloneSetup(employeeLeaveAbsenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        employeeLeaveAbsence = new EmployeeLeaveAbsence();
        employeeLeaveAbsence.setAbsentFrom(DEFAULT_ABSENT_FROM);
        employeeLeaveAbsence.setAbsentTo(DEFAULT_ABSENT_TO);
    }

    @Test
    @Transactional
    public void createEmployeeLeaveAbsence() throws Exception {
        int databaseSizeBeforeCreate = employeeLeaveAbsenceRepository.findAll().size();

        // Create the EmployeeLeaveAbsence

        restEmployeeLeaveAbsenceMockMvc.perform(post("/api/employee-leave-absences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeLeaveAbsence)))
                .andExpect(status().isCreated());

        // Validate the EmployeeLeaveAbsence in the database
        List<EmployeeLeaveAbsence> employeeLeaveAbsences = employeeLeaveAbsenceRepository.findAll();
        assertThat(employeeLeaveAbsences).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeLeaveAbsence testEmployeeLeaveAbsence = employeeLeaveAbsences.get(employeeLeaveAbsences.size() - 1);
        assertThat(testEmployeeLeaveAbsence.getAbsentFrom()).isEqualTo(DEFAULT_ABSENT_FROM);
        assertThat(testEmployeeLeaveAbsence.getAbsentTo()).isEqualTo(DEFAULT_ABSENT_TO);
    }

    @Test
    @Transactional
    public void getAllEmployeeLeaveAbsences() throws Exception {
        // Initialize the database
        employeeLeaveAbsenceRepository.saveAndFlush(employeeLeaveAbsence);

        // Get all the employeeLeaveAbsences
        restEmployeeLeaveAbsenceMockMvc.perform(get("/api/employee-leave-absences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employeeLeaveAbsence.getId().intValue())))
                .andExpect(jsonPath("$.[*].absentFrom").value(hasItem(DEFAULT_ABSENT_FROM_STR)))
                .andExpect(jsonPath("$.[*].absentTo").value(hasItem(DEFAULT_ABSENT_TO_STR)));
    }

    @Test
    @Transactional
    public void getEmployeeLeaveAbsence() throws Exception {
        // Initialize the database
        employeeLeaveAbsenceRepository.saveAndFlush(employeeLeaveAbsence);

        // Get the employeeLeaveAbsence
        restEmployeeLeaveAbsenceMockMvc.perform(get("/api/employee-leave-absences/{id}", employeeLeaveAbsence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employeeLeaveAbsence.getId().intValue()))
            .andExpect(jsonPath("$.absentFrom").value(DEFAULT_ABSENT_FROM_STR))
            .andExpect(jsonPath("$.absentTo").value(DEFAULT_ABSENT_TO_STR));
    }

    @Test
    @Transactional
    public void getNonExistingEmployeeLeaveAbsence() throws Exception {
        // Get the employeeLeaveAbsence
        restEmployeeLeaveAbsenceMockMvc.perform(get("/api/employee-leave-absences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeLeaveAbsence() throws Exception {
        // Initialize the database
        employeeLeaveAbsenceRepository.saveAndFlush(employeeLeaveAbsence);
        int databaseSizeBeforeUpdate = employeeLeaveAbsenceRepository.findAll().size();

        // Update the employeeLeaveAbsence
        EmployeeLeaveAbsence updatedEmployeeLeaveAbsence = new EmployeeLeaveAbsence();
        updatedEmployeeLeaveAbsence.setId(employeeLeaveAbsence.getId());
        updatedEmployeeLeaveAbsence.setAbsentFrom(UPDATED_ABSENT_FROM);
        updatedEmployeeLeaveAbsence.setAbsentTo(UPDATED_ABSENT_TO);

        restEmployeeLeaveAbsenceMockMvc.perform(put("/api/employee-leave-absences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeLeaveAbsence)))
                .andExpect(status().isOk());

        // Validate the EmployeeLeaveAbsence in the database
        List<EmployeeLeaveAbsence> employeeLeaveAbsences = employeeLeaveAbsenceRepository.findAll();
        assertThat(employeeLeaveAbsences).hasSize(databaseSizeBeforeUpdate);
        EmployeeLeaveAbsence testEmployeeLeaveAbsence = employeeLeaveAbsences.get(employeeLeaveAbsences.size() - 1);
        assertThat(testEmployeeLeaveAbsence.getAbsentFrom()).isEqualTo(UPDATED_ABSENT_FROM);
        assertThat(testEmployeeLeaveAbsence.getAbsentTo()).isEqualTo(UPDATED_ABSENT_TO);
    }

    @Test
    @Transactional
    public void deleteEmployeeLeaveAbsence() throws Exception {
        // Initialize the database
        employeeLeaveAbsenceRepository.saveAndFlush(employeeLeaveAbsence);
        int databaseSizeBeforeDelete = employeeLeaveAbsenceRepository.findAll().size();

        // Get the employeeLeaveAbsence
        restEmployeeLeaveAbsenceMockMvc.perform(delete("/api/employee-leave-absences/{id}", employeeLeaveAbsence.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EmployeeLeaveAbsence> employeeLeaveAbsences = employeeLeaveAbsenceRepository.findAll();
        assertThat(employeeLeaveAbsences).hasSize(databaseSizeBeforeDelete - 1);
    }
}
