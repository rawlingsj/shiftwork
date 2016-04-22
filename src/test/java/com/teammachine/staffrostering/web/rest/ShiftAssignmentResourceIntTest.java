package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;

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
 * Test class for the ShiftAssignmentResource REST controller.
 *
 * @see ShiftAssignmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class ShiftAssignmentResourceIntTest {


    private static final Integer DEFAULT_INDEX_IN_SHIFT = 1;
    private static final Integer UPDATED_INDEX_IN_SHIFT = 2;

    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restShiftAssignmentMockMvc;

    private ShiftAssignment shiftAssignment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShiftAssignmentResource shiftAssignmentResource = new ShiftAssignmentResource();
        ReflectionTestUtils.setField(shiftAssignmentResource, "shiftAssignmentRepository", shiftAssignmentRepository);
        this.restShiftAssignmentMockMvc = MockMvcBuilders.standaloneSetup(shiftAssignmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        shiftAssignment = new ShiftAssignment();
        shiftAssignment.setIndexInShift(DEFAULT_INDEX_IN_SHIFT);
    }

    @Test
    @Transactional
    public void createShiftAssignment() throws Exception {
        int databaseSizeBeforeCreate = shiftAssignmentRepository.findAll().size();

        // Create the ShiftAssignment

        restShiftAssignmentMockMvc.perform(post("/api/shift-assignments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shiftAssignment)))
                .andExpect(status().isCreated());

        // Validate the ShiftAssignment in the database
        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findAll();
        assertThat(shiftAssignments).hasSize(databaseSizeBeforeCreate + 1);
        ShiftAssignment testShiftAssignment = shiftAssignments.get(shiftAssignments.size() - 1);
        assertThat(testShiftAssignment.getIndexInShift()).isEqualTo(DEFAULT_INDEX_IN_SHIFT);
    }

    @Test
    @Transactional
    public void getAllShiftAssignments() throws Exception {
        // Initialize the database
        shiftAssignmentRepository.saveAndFlush(shiftAssignment);

        // Get all the shiftAssignments
        restShiftAssignmentMockMvc.perform(get("/api/shift-assignments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(shiftAssignment.getId().intValue())))
                .andExpect(jsonPath("$.[*].indexInShift").value(hasItem(DEFAULT_INDEX_IN_SHIFT)));
    }

    @Test
    @Transactional
    public void getShiftAssignment() throws Exception {
        // Initialize the database
        shiftAssignmentRepository.saveAndFlush(shiftAssignment);

        // Get the shiftAssignment
        restShiftAssignmentMockMvc.perform(get("/api/shift-assignments/{id}", shiftAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(shiftAssignment.getId().intValue()))
            .andExpect(jsonPath("$.indexInShift").value(DEFAULT_INDEX_IN_SHIFT));
    }

    @Test
    @Transactional
    public void getNonExistingShiftAssignment() throws Exception {
        // Get the shiftAssignment
        restShiftAssignmentMockMvc.perform(get("/api/shift-assignments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShiftAssignment() throws Exception {
        // Initialize the database
        shiftAssignmentRepository.saveAndFlush(shiftAssignment);
        int databaseSizeBeforeUpdate = shiftAssignmentRepository.findAll().size();

        // Update the shiftAssignment
        ShiftAssignment updatedShiftAssignment = new ShiftAssignment();
        updatedShiftAssignment.setId(shiftAssignment.getId());
        updatedShiftAssignment.setIndexInShift(UPDATED_INDEX_IN_SHIFT);

        restShiftAssignmentMockMvc.perform(put("/api/shift-assignments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedShiftAssignment)))
                .andExpect(status().isOk());

        // Validate the ShiftAssignment in the database
        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findAll();
        assertThat(shiftAssignments).hasSize(databaseSizeBeforeUpdate);
        ShiftAssignment testShiftAssignment = shiftAssignments.get(shiftAssignments.size() - 1);
        assertThat(testShiftAssignment.getIndexInShift()).isEqualTo(UPDATED_INDEX_IN_SHIFT);
    }

    @Test
    @Transactional
    public void deleteShiftAssignment() throws Exception {
        // Initialize the database
        shiftAssignmentRepository.saveAndFlush(shiftAssignment);
        int databaseSizeBeforeDelete = shiftAssignmentRepository.findAll().size();

        // Get the shiftAssignment
        restShiftAssignmentMockMvc.perform(delete("/api/shift-assignments/{id}", shiftAssignment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findAll();
        assertThat(shiftAssignments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
