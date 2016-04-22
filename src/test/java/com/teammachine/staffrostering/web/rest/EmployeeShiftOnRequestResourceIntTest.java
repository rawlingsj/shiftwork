package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.EmployeeShiftOnRequest;
import com.teammachine.staffrostering.repository.EmployeeShiftOnRequestRepository;

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
 * Test class for the EmployeeShiftOnRequestResource REST controller.
 *
 * @see EmployeeShiftOnRequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class EmployeeShiftOnRequestResourceIntTest {


    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    @Inject
    private EmployeeShiftOnRequestRepository employeeShiftOnRequestRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeeShiftOnRequestMockMvc;

    private EmployeeShiftOnRequest employeeShiftOnRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeShiftOnRequestResource employeeShiftOnRequestResource = new EmployeeShiftOnRequestResource();
        ReflectionTestUtils.setField(employeeShiftOnRequestResource, "employeeShiftOnRequestRepository", employeeShiftOnRequestRepository);
        this.restEmployeeShiftOnRequestMockMvc = MockMvcBuilders.standaloneSetup(employeeShiftOnRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        employeeShiftOnRequest = new EmployeeShiftOnRequest();
        employeeShiftOnRequest.setWeight(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    public void createEmployeeShiftOnRequest() throws Exception {
        int databaseSizeBeforeCreate = employeeShiftOnRequestRepository.findAll().size();

        // Create the EmployeeShiftOnRequest

        restEmployeeShiftOnRequestMockMvc.perform(post("/api/employee-shift-on-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeShiftOnRequest)))
                .andExpect(status().isCreated());

        // Validate the EmployeeShiftOnRequest in the database
        List<EmployeeShiftOnRequest> employeeShiftOnRequests = employeeShiftOnRequestRepository.findAll();
        assertThat(employeeShiftOnRequests).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeShiftOnRequest testEmployeeShiftOnRequest = employeeShiftOnRequests.get(employeeShiftOnRequests.size() - 1);
        assertThat(testEmployeeShiftOnRequest.getWeight()).isEqualTo(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllEmployeeShiftOnRequests() throws Exception {
        // Initialize the database
        employeeShiftOnRequestRepository.saveAndFlush(employeeShiftOnRequest);

        // Get all the employeeShiftOnRequests
        restEmployeeShiftOnRequestMockMvc.perform(get("/api/employee-shift-on-requests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employeeShiftOnRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }

    @Test
    @Transactional
    public void getEmployeeShiftOnRequest() throws Exception {
        // Initialize the database
        employeeShiftOnRequestRepository.saveAndFlush(employeeShiftOnRequest);

        // Get the employeeShiftOnRequest
        restEmployeeShiftOnRequestMockMvc.perform(get("/api/employee-shift-on-requests/{id}", employeeShiftOnRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employeeShiftOnRequest.getId().intValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT));
    }

    @Test
    @Transactional
    public void getNonExistingEmployeeShiftOnRequest() throws Exception {
        // Get the employeeShiftOnRequest
        restEmployeeShiftOnRequestMockMvc.perform(get("/api/employee-shift-on-requests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeShiftOnRequest() throws Exception {
        // Initialize the database
        employeeShiftOnRequestRepository.saveAndFlush(employeeShiftOnRequest);
        int databaseSizeBeforeUpdate = employeeShiftOnRequestRepository.findAll().size();

        // Update the employeeShiftOnRequest
        EmployeeShiftOnRequest updatedEmployeeShiftOnRequest = new EmployeeShiftOnRequest();
        updatedEmployeeShiftOnRequest.setId(employeeShiftOnRequest.getId());
        updatedEmployeeShiftOnRequest.setWeight(UPDATED_WEIGHT);

        restEmployeeShiftOnRequestMockMvc.perform(put("/api/employee-shift-on-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeShiftOnRequest)))
                .andExpect(status().isOk());

        // Validate the EmployeeShiftOnRequest in the database
        List<EmployeeShiftOnRequest> employeeShiftOnRequests = employeeShiftOnRequestRepository.findAll();
        assertThat(employeeShiftOnRequests).hasSize(databaseSizeBeforeUpdate);
        EmployeeShiftOnRequest testEmployeeShiftOnRequest = employeeShiftOnRequests.get(employeeShiftOnRequests.size() - 1);
        assertThat(testEmployeeShiftOnRequest.getWeight()).isEqualTo(UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void deleteEmployeeShiftOnRequest() throws Exception {
        // Initialize the database
        employeeShiftOnRequestRepository.saveAndFlush(employeeShiftOnRequest);
        int databaseSizeBeforeDelete = employeeShiftOnRequestRepository.findAll().size();

        // Get the employeeShiftOnRequest
        restEmployeeShiftOnRequestMockMvc.perform(delete("/api/employee-shift-on-requests/{id}", employeeShiftOnRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EmployeeShiftOnRequest> employeeShiftOnRequests = employeeShiftOnRequestRepository.findAll();
        assertThat(employeeShiftOnRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
