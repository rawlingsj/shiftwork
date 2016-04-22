package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.repository.StaffRosterParametrizationRepository;

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
 * Test class for the StaffRosterParametrizationResource REST controller.
 *
 * @see StaffRosterParametrizationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class StaffRosterParametrizationResourceIntTest {


    @Inject
    private StaffRosterParametrizationRepository staffRosterParametrizationRepository;

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
        this.restStaffRosterParametrizationMockMvc = MockMvcBuilders.standaloneSetup(staffRosterParametrizationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        staffRosterParametrization = new StaffRosterParametrization();
    }

    @Test
    @Transactional
    public void createStaffRosterParametrization() throws Exception {
        int databaseSizeBeforeCreate = staffRosterParametrizationRepository.findAll().size();

        // Create the StaffRosterParametrization

        restStaffRosterParametrizationMockMvc.perform(post("/api/staff-roster-parametrizations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(staffRosterParametrization)))
                .andExpect(status().isCreated());

        // Validate the StaffRosterParametrization in the database
        List<StaffRosterParametrization> staffRosterParametrizations = staffRosterParametrizationRepository.findAll();
        assertThat(staffRosterParametrizations).hasSize(databaseSizeBeforeCreate + 1);
        StaffRosterParametrization testStaffRosterParametrization = staffRosterParametrizations.get(staffRosterParametrizations.size() - 1);
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
                .andExpect(jsonPath("$.[*].id").value(hasItem(staffRosterParametrization.getId().intValue())));
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
            .andExpect(jsonPath("$.id").value(staffRosterParametrization.getId().intValue()));
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

        restStaffRosterParametrizationMockMvc.perform(put("/api/staff-roster-parametrizations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStaffRosterParametrization)))
                .andExpect(status().isOk());

        // Validate the StaffRosterParametrization in the database
        List<StaffRosterParametrization> staffRosterParametrizations = staffRosterParametrizationRepository.findAll();
        assertThat(staffRosterParametrizations).hasSize(databaseSizeBeforeUpdate);
        StaffRosterParametrization testStaffRosterParametrization = staffRosterParametrizations.get(staffRosterParametrizations.size() - 1);
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
