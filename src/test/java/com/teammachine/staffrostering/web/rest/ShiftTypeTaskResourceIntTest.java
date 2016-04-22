package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.ShiftTypeTask;
import com.teammachine.staffrostering.repository.ShiftTypeTaskRepository;

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
 * Test class for the ShiftTypeTaskResource REST controller.
 *
 * @see ShiftTypeTaskResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class ShiftTypeTaskResourceIntTest {


    @Inject
    private ShiftTypeTaskRepository shiftTypeTaskRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restShiftTypeTaskMockMvc;

    private ShiftTypeTask shiftTypeTask;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShiftTypeTaskResource shiftTypeTaskResource = new ShiftTypeTaskResource();
        ReflectionTestUtils.setField(shiftTypeTaskResource, "shiftTypeTaskRepository", shiftTypeTaskRepository);
        this.restShiftTypeTaskMockMvc = MockMvcBuilders.standaloneSetup(shiftTypeTaskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        shiftTypeTask = new ShiftTypeTask();
    }

    @Test
    @Transactional
    public void createShiftTypeTask() throws Exception {
        int databaseSizeBeforeCreate = shiftTypeTaskRepository.findAll().size();

        // Create the ShiftTypeTask

        restShiftTypeTaskMockMvc.perform(post("/api/shift-type-tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shiftTypeTask)))
                .andExpect(status().isCreated());

        // Validate the ShiftTypeTask in the database
        List<ShiftTypeTask> shiftTypeTasks = shiftTypeTaskRepository.findAll();
        assertThat(shiftTypeTasks).hasSize(databaseSizeBeforeCreate + 1);
        ShiftTypeTask testShiftTypeTask = shiftTypeTasks.get(shiftTypeTasks.size() - 1);
    }

    @Test
    @Transactional
    public void getAllShiftTypeTasks() throws Exception {
        // Initialize the database
        shiftTypeTaskRepository.saveAndFlush(shiftTypeTask);

        // Get all the shiftTypeTasks
        restShiftTypeTaskMockMvc.perform(get("/api/shift-type-tasks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(shiftTypeTask.getId().intValue())));
    }

    @Test
    @Transactional
    public void getShiftTypeTask() throws Exception {
        // Initialize the database
        shiftTypeTaskRepository.saveAndFlush(shiftTypeTask);

        // Get the shiftTypeTask
        restShiftTypeTaskMockMvc.perform(get("/api/shift-type-tasks/{id}", shiftTypeTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(shiftTypeTask.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingShiftTypeTask() throws Exception {
        // Get the shiftTypeTask
        restShiftTypeTaskMockMvc.perform(get("/api/shift-type-tasks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShiftTypeTask() throws Exception {
        // Initialize the database
        shiftTypeTaskRepository.saveAndFlush(shiftTypeTask);
        int databaseSizeBeforeUpdate = shiftTypeTaskRepository.findAll().size();

        // Update the shiftTypeTask
        ShiftTypeTask updatedShiftTypeTask = new ShiftTypeTask();
        updatedShiftTypeTask.setId(shiftTypeTask.getId());

        restShiftTypeTaskMockMvc.perform(put("/api/shift-type-tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedShiftTypeTask)))
                .andExpect(status().isOk());

        // Validate the ShiftTypeTask in the database
        List<ShiftTypeTask> shiftTypeTasks = shiftTypeTaskRepository.findAll();
        assertThat(shiftTypeTasks).hasSize(databaseSizeBeforeUpdate);
        ShiftTypeTask testShiftTypeTask = shiftTypeTasks.get(shiftTypeTasks.size() - 1);
    }

    @Test
    @Transactional
    public void deleteShiftTypeTask() throws Exception {
        // Initialize the database
        shiftTypeTaskRepository.saveAndFlush(shiftTypeTask);
        int databaseSizeBeforeDelete = shiftTypeTaskRepository.findAll().size();

        // Get the shiftTypeTask
        restShiftTypeTaskMockMvc.perform(delete("/api/shift-type-tasks/{id}", shiftTypeTask.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ShiftTypeTask> shiftTypeTasks = shiftTypeTaskRepository.findAll();
        assertThat(shiftTypeTasks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
