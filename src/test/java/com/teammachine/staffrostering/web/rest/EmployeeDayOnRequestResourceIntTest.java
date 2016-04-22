package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.EmployeeDayOnRequest;
import com.teammachine.staffrostering.repository.EmployeeDayOnRequestRepository;

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
 * Test class for the EmployeeDayOnRequestResource REST controller.
 *
 * @see EmployeeDayOnRequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class EmployeeDayOnRequestResourceIntTest {


    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    @Inject
    private EmployeeDayOnRequestRepository employeeDayOnRequestRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeeDayOnRequestMockMvc;

    private EmployeeDayOnRequest employeeDayOnRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeDayOnRequestResource employeeDayOnRequestResource = new EmployeeDayOnRequestResource();
        ReflectionTestUtils.setField(employeeDayOnRequestResource, "employeeDayOnRequestRepository", employeeDayOnRequestRepository);
        this.restEmployeeDayOnRequestMockMvc = MockMvcBuilders.standaloneSetup(employeeDayOnRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        employeeDayOnRequest = new EmployeeDayOnRequest();
        employeeDayOnRequest.setWeight(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    public void createEmployeeDayOnRequest() throws Exception {
        int databaseSizeBeforeCreate = employeeDayOnRequestRepository.findAll().size();

        // Create the EmployeeDayOnRequest

        restEmployeeDayOnRequestMockMvc.perform(post("/api/employee-day-on-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeDayOnRequest)))
                .andExpect(status().isCreated());

        // Validate the EmployeeDayOnRequest in the database
        List<EmployeeDayOnRequest> employeeDayOnRequests = employeeDayOnRequestRepository.findAll();
        assertThat(employeeDayOnRequests).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeDayOnRequest testEmployeeDayOnRequest = employeeDayOnRequests.get(employeeDayOnRequests.size() - 1);
        assertThat(testEmployeeDayOnRequest.getWeight()).isEqualTo(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllEmployeeDayOnRequests() throws Exception {
        // Initialize the database
        employeeDayOnRequestRepository.saveAndFlush(employeeDayOnRequest);

        // Get all the employeeDayOnRequests
        restEmployeeDayOnRequestMockMvc.perform(get("/api/employee-day-on-requests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employeeDayOnRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }

    @Test
    @Transactional
    public void getEmployeeDayOnRequest() throws Exception {
        // Initialize the database
        employeeDayOnRequestRepository.saveAndFlush(employeeDayOnRequest);

        // Get the employeeDayOnRequest
        restEmployeeDayOnRequestMockMvc.perform(get("/api/employee-day-on-requests/{id}", employeeDayOnRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employeeDayOnRequest.getId().intValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT));
    }

    @Test
    @Transactional
    public void getNonExistingEmployeeDayOnRequest() throws Exception {
        // Get the employeeDayOnRequest
        restEmployeeDayOnRequestMockMvc.perform(get("/api/employee-day-on-requests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeDayOnRequest() throws Exception {
        // Initialize the database
        employeeDayOnRequestRepository.saveAndFlush(employeeDayOnRequest);
        int databaseSizeBeforeUpdate = employeeDayOnRequestRepository.findAll().size();

        // Update the employeeDayOnRequest
        EmployeeDayOnRequest updatedEmployeeDayOnRequest = new EmployeeDayOnRequest();
        updatedEmployeeDayOnRequest.setId(employeeDayOnRequest.getId());
        updatedEmployeeDayOnRequest.setWeight(UPDATED_WEIGHT);

        restEmployeeDayOnRequestMockMvc.perform(put("/api/employee-day-on-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeDayOnRequest)))
                .andExpect(status().isOk());

        // Validate the EmployeeDayOnRequest in the database
        List<EmployeeDayOnRequest> employeeDayOnRequests = employeeDayOnRequestRepository.findAll();
        assertThat(employeeDayOnRequests).hasSize(databaseSizeBeforeUpdate);
        EmployeeDayOnRequest testEmployeeDayOnRequest = employeeDayOnRequests.get(employeeDayOnRequests.size() - 1);
        assertThat(testEmployeeDayOnRequest.getWeight()).isEqualTo(UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void deleteEmployeeDayOnRequest() throws Exception {
        // Initialize the database
        employeeDayOnRequestRepository.saveAndFlush(employeeDayOnRequest);
        int databaseSizeBeforeDelete = employeeDayOnRequestRepository.findAll().size();

        // Get the employeeDayOnRequest
        restEmployeeDayOnRequestMockMvc.perform(delete("/api/employee-day-on-requests/{id}", employeeDayOnRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EmployeeDayOnRequest> employeeDayOnRequests = employeeDayOnRequestRepository.findAll();
        assertThat(employeeDayOnRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
