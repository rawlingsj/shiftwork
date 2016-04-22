package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.EmployeeShiftOffRequest;
import com.teammachine.staffrostering.repository.EmployeeShiftOffRequestRepository;

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
 * Test class for the EmployeeShiftOffRequestResource REST controller.
 *
 * @see EmployeeShiftOffRequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class EmployeeShiftOffRequestResourceIntTest {


    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    @Inject
    private EmployeeShiftOffRequestRepository employeeShiftOffRequestRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeeShiftOffRequestMockMvc;

    private EmployeeShiftOffRequest employeeShiftOffRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeShiftOffRequestResource employeeShiftOffRequestResource = new EmployeeShiftOffRequestResource();
        ReflectionTestUtils.setField(employeeShiftOffRequestResource, "employeeShiftOffRequestRepository", employeeShiftOffRequestRepository);
        this.restEmployeeShiftOffRequestMockMvc = MockMvcBuilders.standaloneSetup(employeeShiftOffRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        employeeShiftOffRequest = new EmployeeShiftOffRequest();
        employeeShiftOffRequest.setWeight(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    public void createEmployeeShiftOffRequest() throws Exception {
        int databaseSizeBeforeCreate = employeeShiftOffRequestRepository.findAll().size();

        // Create the EmployeeShiftOffRequest

        restEmployeeShiftOffRequestMockMvc.perform(post("/api/employee-shift-off-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeShiftOffRequest)))
                .andExpect(status().isCreated());

        // Validate the EmployeeShiftOffRequest in the database
        List<EmployeeShiftOffRequest> employeeShiftOffRequests = employeeShiftOffRequestRepository.findAll();
        assertThat(employeeShiftOffRequests).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeShiftOffRequest testEmployeeShiftOffRequest = employeeShiftOffRequests.get(employeeShiftOffRequests.size() - 1);
        assertThat(testEmployeeShiftOffRequest.getWeight()).isEqualTo(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllEmployeeShiftOffRequests() throws Exception {
        // Initialize the database
        employeeShiftOffRequestRepository.saveAndFlush(employeeShiftOffRequest);

        // Get all the employeeShiftOffRequests
        restEmployeeShiftOffRequestMockMvc.perform(get("/api/employee-shift-off-requests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employeeShiftOffRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }

    @Test
    @Transactional
    public void getEmployeeShiftOffRequest() throws Exception {
        // Initialize the database
        employeeShiftOffRequestRepository.saveAndFlush(employeeShiftOffRequest);

        // Get the employeeShiftOffRequest
        restEmployeeShiftOffRequestMockMvc.perform(get("/api/employee-shift-off-requests/{id}", employeeShiftOffRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employeeShiftOffRequest.getId().intValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT));
    }

    @Test
    @Transactional
    public void getNonExistingEmployeeShiftOffRequest() throws Exception {
        // Get the employeeShiftOffRequest
        restEmployeeShiftOffRequestMockMvc.perform(get("/api/employee-shift-off-requests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeShiftOffRequest() throws Exception {
        // Initialize the database
        employeeShiftOffRequestRepository.saveAndFlush(employeeShiftOffRequest);
        int databaseSizeBeforeUpdate = employeeShiftOffRequestRepository.findAll().size();

        // Update the employeeShiftOffRequest
        EmployeeShiftOffRequest updatedEmployeeShiftOffRequest = new EmployeeShiftOffRequest();
        updatedEmployeeShiftOffRequest.setId(employeeShiftOffRequest.getId());
        updatedEmployeeShiftOffRequest.setWeight(UPDATED_WEIGHT);

        restEmployeeShiftOffRequestMockMvc.perform(put("/api/employee-shift-off-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeShiftOffRequest)))
                .andExpect(status().isOk());

        // Validate the EmployeeShiftOffRequest in the database
        List<EmployeeShiftOffRequest> employeeShiftOffRequests = employeeShiftOffRequestRepository.findAll();
        assertThat(employeeShiftOffRequests).hasSize(databaseSizeBeforeUpdate);
        EmployeeShiftOffRequest testEmployeeShiftOffRequest = employeeShiftOffRequests.get(employeeShiftOffRequests.size() - 1);
        assertThat(testEmployeeShiftOffRequest.getWeight()).isEqualTo(UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void deleteEmployeeShiftOffRequest() throws Exception {
        // Initialize the database
        employeeShiftOffRequestRepository.saveAndFlush(employeeShiftOffRequest);
        int databaseSizeBeforeDelete = employeeShiftOffRequestRepository.findAll().size();

        // Get the employeeShiftOffRequest
        restEmployeeShiftOffRequestMockMvc.perform(delete("/api/employee-shift-off-requests/{id}", employeeShiftOffRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EmployeeShiftOffRequest> employeeShiftOffRequests = employeeShiftOffRequestRepository.findAll();
        assertThat(employeeShiftOffRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
