package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.EmployeeDayOffRequest;
import com.teammachine.staffrostering.repository.EmployeeDayOffRequestRepository;

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
 * Test class for the EmployeeDayOffRequestResource REST controller.
 *
 * @see EmployeeDayOffRequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class EmployeeDayOffRequestResourceIntTest {


    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    @Inject
    private EmployeeDayOffRequestRepository employeeDayOffRequestRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeeDayOffRequestMockMvc;

    private EmployeeDayOffRequest employeeDayOffRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeDayOffRequestResource employeeDayOffRequestResource = new EmployeeDayOffRequestResource();
        ReflectionTestUtils.setField(employeeDayOffRequestResource, "employeeDayOffRequestRepository", employeeDayOffRequestRepository);
        this.restEmployeeDayOffRequestMockMvc = MockMvcBuilders.standaloneSetup(employeeDayOffRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        employeeDayOffRequest = new EmployeeDayOffRequest();
        employeeDayOffRequest.setWeight(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    public void createEmployeeDayOffRequest() throws Exception {
        int databaseSizeBeforeCreate = employeeDayOffRequestRepository.findAll().size();

        // Create the EmployeeDayOffRequest

        restEmployeeDayOffRequestMockMvc.perform(post("/api/employee-day-off-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeDayOffRequest)))
                .andExpect(status().isCreated());

        // Validate the EmployeeDayOffRequest in the database
        List<EmployeeDayOffRequest> employeeDayOffRequests = employeeDayOffRequestRepository.findAll();
        assertThat(employeeDayOffRequests).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeDayOffRequest testEmployeeDayOffRequest = employeeDayOffRequests.get(employeeDayOffRequests.size() - 1);
        assertThat(testEmployeeDayOffRequest.getWeight()).isEqualTo(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllEmployeeDayOffRequests() throws Exception {
        // Initialize the database
        employeeDayOffRequestRepository.saveAndFlush(employeeDayOffRequest);

        // Get all the employeeDayOffRequests
        restEmployeeDayOffRequestMockMvc.perform(get("/api/employee-day-off-requests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employeeDayOffRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }

    @Test
    @Transactional
    public void getEmployeeDayOffRequest() throws Exception {
        // Initialize the database
        employeeDayOffRequestRepository.saveAndFlush(employeeDayOffRequest);

        // Get the employeeDayOffRequest
        restEmployeeDayOffRequestMockMvc.perform(get("/api/employee-day-off-requests/{id}", employeeDayOffRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employeeDayOffRequest.getId().intValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT));
    }

    @Test
    @Transactional
    public void getNonExistingEmployeeDayOffRequest() throws Exception {
        // Get the employeeDayOffRequest
        restEmployeeDayOffRequestMockMvc.perform(get("/api/employee-day-off-requests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeDayOffRequest() throws Exception {
        // Initialize the database
        employeeDayOffRequestRepository.saveAndFlush(employeeDayOffRequest);
        int databaseSizeBeforeUpdate = employeeDayOffRequestRepository.findAll().size();

        // Update the employeeDayOffRequest
        EmployeeDayOffRequest updatedEmployeeDayOffRequest = new EmployeeDayOffRequest();
        updatedEmployeeDayOffRequest.setId(employeeDayOffRequest.getId());
        updatedEmployeeDayOffRequest.setWeight(UPDATED_WEIGHT);

        restEmployeeDayOffRequestMockMvc.perform(put("/api/employee-day-off-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeDayOffRequest)))
                .andExpect(status().isOk());

        // Validate the EmployeeDayOffRequest in the database
        List<EmployeeDayOffRequest> employeeDayOffRequests = employeeDayOffRequestRepository.findAll();
        assertThat(employeeDayOffRequests).hasSize(databaseSizeBeforeUpdate);
        EmployeeDayOffRequest testEmployeeDayOffRequest = employeeDayOffRequests.get(employeeDayOffRequests.size() - 1);
        assertThat(testEmployeeDayOffRequest.getWeight()).isEqualTo(UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void deleteEmployeeDayOffRequest() throws Exception {
        // Initialize the database
        employeeDayOffRequestRepository.saveAndFlush(employeeDayOffRequest);
        int databaseSizeBeforeDelete = employeeDayOffRequestRepository.findAll().size();

        // Get the employeeDayOffRequest
        restEmployeeDayOffRequestMockMvc.perform(delete("/api/employee-day-off-requests/{id}", employeeDayOffRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EmployeeDayOffRequest> employeeDayOffRequests = employeeDayOffRequestRepository.findAll();
        assertThat(employeeDayOffRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
