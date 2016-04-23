package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.Employees;
import com.teammachine.staffrostering.repository.EmployeesRepository;

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
 * Test class for the EmployeesResource REST controller.
 *
 * @see EmployeesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class EmployeesResourceIntTest {


    @Inject
    private EmployeesRepository employeesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeesMockMvc;

    private Employees employees;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeesResource employeesResource = new EmployeesResource();
        ReflectionTestUtils.setField(employeesResource, "employeesRepository", employeesRepository);
        this.restEmployeesMockMvc = MockMvcBuilders.standaloneSetup(employeesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        employees = new Employees();
    }

    @Test
    @Transactional
    public void createEmployees() throws Exception {
        int databaseSizeBeforeCreate = employeesRepository.findAll().size();

        // Create the Employees

        restEmployeesMockMvc.perform(post("/api/employees2")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employees)))
                .andExpect(status().isCreated());

        // Validate the Employees in the database
        List<Employees> employees = employeesRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeCreate + 1);
        Employees testEmployees = employees.get(employees.size() - 1);
    }

    @Test
    @Transactional
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employees
        restEmployeesMockMvc.perform(get("/api/employees2?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employees.getId().intValue())));
    }

    @Test
    @Transactional
    public void getEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get the employees
        restEmployeesMockMvc.perform(get("/api/employees2/{id}", employees.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employees.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployees() throws Exception {
        // Get the employees
        restEmployeesMockMvc.perform(get("/api/employees2/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);
        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();

        // Update the employees
        Employees updatedEmployees = new Employees();
        updatedEmployees.setId(employees.getId());

        restEmployeesMockMvc.perform(put("/api/employees2")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmployees)))
                .andExpect(status().isOk());

        // Validate the Employees in the database
        List<Employees> employees = employeesRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeUpdate);
        Employees testEmployees = employees.get(employees.size() - 1);
    }

    @Test
    @Transactional
    public void deleteEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);
        int databaseSizeBeforeDelete = employeesRepository.findAll().size();

        // Get the employees
        restEmployeesMockMvc.perform(delete("/api/employees2/{id}", employees.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Employees> employees = employeesRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeDelete - 1);
    }
}
