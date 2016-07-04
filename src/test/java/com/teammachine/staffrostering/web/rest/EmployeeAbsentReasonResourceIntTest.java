package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.EmployeeAbsentReason;
import com.teammachine.staffrostering.repository.EmployeeAbsentReasonRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EmployeeAbsentReasonResource REST controller.
 *
 * @see EmployeeAbsentReasonResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class EmployeeAbsentReasonResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_DEFAULT_DURATION = "AAAAA";
    private static final String UPDATED_DEFAULT_DURATION = "BBBBB";

    @Inject
    private EmployeeAbsentReasonRepository employeeAbsentReasonRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeeAbsentReasonMockMvc;

    private EmployeeAbsentReason employeeAbsentReason;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeAbsentReasonResource employeeAbsentReasonResource = new EmployeeAbsentReasonResource();
        ReflectionTestUtils.setField(employeeAbsentReasonResource, "employeeAbsentReasonRepository", employeeAbsentReasonRepository);
        this.restEmployeeAbsentReasonMockMvc = MockMvcBuilders.standaloneSetup(employeeAbsentReasonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        employeeAbsentReason = new EmployeeAbsentReason();
        employeeAbsentReason.setCode(DEFAULT_CODE);
        employeeAbsentReason.setName(DEFAULT_NAME);
        employeeAbsentReason.setDescription(DEFAULT_DESCRIPTION);
        employeeAbsentReason.setDefaultDuration(DEFAULT_DEFAULT_DURATION);
    }

    @Test
    @Transactional
    public void createEmployeeAbsentReason() throws Exception {
        int databaseSizeBeforeCreate = employeeAbsentReasonRepository.findAll().size();

        // Create the EmployeeAbsentReason

        restEmployeeAbsentReasonMockMvc.perform(post("/api/employee-absent-reasons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeAbsentReason)))
                .andExpect(status().isCreated());

        // Validate the EmployeeAbsentReason in the database
        List<EmployeeAbsentReason> employeeAbsentReasons = employeeAbsentReasonRepository.findAll();
        assertThat(employeeAbsentReasons).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeAbsentReason testEmployeeAbsentReason = employeeAbsentReasons.get(employeeAbsentReasons.size() - 1);
        assertThat(testEmployeeAbsentReason.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testEmployeeAbsentReason.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmployeeAbsentReason.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEmployeeAbsentReason.getDefaultDuration()).isEqualTo(DEFAULT_DEFAULT_DURATION);
    }

    @Test
    @Transactional
    public void getAllEmployeeAbsentReasons() throws Exception {
        // Initialize the database
        employeeAbsentReasonRepository.saveAndFlush(employeeAbsentReason);

        // Get all the employeeAbsentReasons
        restEmployeeAbsentReasonMockMvc.perform(get("/api/employee-absent-reasons?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAbsentReason.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].defaultDuration").value(hasItem(DEFAULT_DEFAULT_DURATION.toString())));
    }

    @Test
    @Transactional
    public void getEmployeeAbsentReason() throws Exception {
        // Initialize the database
        employeeAbsentReasonRepository.saveAndFlush(employeeAbsentReason);

        // Get the employeeAbsentReason
        restEmployeeAbsentReasonMockMvc.perform(get("/api/employee-absent-reasons/{id}", employeeAbsentReason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employeeAbsentReason.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.defaultDuration").value(DEFAULT_DEFAULT_DURATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployeeAbsentReason() throws Exception {
        // Get the employeeAbsentReason
        restEmployeeAbsentReasonMockMvc.perform(get("/api/employee-absent-reasons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeAbsentReason() throws Exception {
        // Initialize the database
        employeeAbsentReasonRepository.saveAndFlush(employeeAbsentReason);
        int databaseSizeBeforeUpdate = employeeAbsentReasonRepository.findAll().size();

        // Update the employeeAbsentReason
        EmployeeAbsentReason updatedEmployeeAbsentReason = new EmployeeAbsentReason();
        updatedEmployeeAbsentReason.setId(employeeAbsentReason.getId());
        updatedEmployeeAbsentReason.setCode(UPDATED_CODE);
        updatedEmployeeAbsentReason.setName(UPDATED_NAME);
        updatedEmployeeAbsentReason.setDescription(UPDATED_DESCRIPTION);
        updatedEmployeeAbsentReason.setDefaultDuration(UPDATED_DEFAULT_DURATION);

        restEmployeeAbsentReasonMockMvc.perform(put("/api/employee-absent-reasons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeAbsentReason)))
                .andExpect(status().isOk());

        // Validate the EmployeeAbsentReason in the database
        List<EmployeeAbsentReason> employeeAbsentReasons = employeeAbsentReasonRepository.findAll();
        assertThat(employeeAbsentReasons).hasSize(databaseSizeBeforeUpdate);
        EmployeeAbsentReason testEmployeeAbsentReason = employeeAbsentReasons.get(employeeAbsentReasons.size() - 1);
        assertThat(testEmployeeAbsentReason.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEmployeeAbsentReason.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmployeeAbsentReason.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmployeeAbsentReason.getDefaultDuration()).isEqualTo(UPDATED_DEFAULT_DURATION);
    }

    @Test
    @Transactional
    public void deleteEmployeeAbsentReason() throws Exception {
        // Initialize the database
        employeeAbsentReasonRepository.saveAndFlush(employeeAbsentReason);
        int databaseSizeBeforeDelete = employeeAbsentReasonRepository.findAll().size();

        // Get the employeeAbsentReason
        restEmployeeAbsentReasonMockMvc.perform(delete("/api/employee-absent-reasons/{id}", employeeAbsentReason.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EmployeeAbsentReason> employeeAbsentReasons = employeeAbsentReasonRepository.findAll();
        assertThat(employeeAbsentReasons).hasSize(databaseSizeBeforeDelete - 1);
    }
}
