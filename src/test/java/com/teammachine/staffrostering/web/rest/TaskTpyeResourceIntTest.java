package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.TaskTpye;
import com.teammachine.staffrostering.repository.TaskTpyeRepository;

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
 * Test class for the TaskTpyeResource REST controller.
 *
 * @see TaskTpyeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class TaskTpyeResourceIntTest {


    @Inject
    private TaskTpyeRepository taskTpyeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaskTpyeMockMvc;

    private TaskTpye taskTpye;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskTpyeResource taskTpyeResource = new TaskTpyeResource();
        ReflectionTestUtils.setField(taskTpyeResource, "taskTpyeRepository", taskTpyeRepository);
        this.restTaskTpyeMockMvc = MockMvcBuilders.standaloneSetup(taskTpyeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        taskTpye = new TaskTpye();
    }

    @Test
    @Transactional
    public void createTaskTpye() throws Exception {
        int databaseSizeBeforeCreate = taskTpyeRepository.findAll().size();

        // Create the TaskTpye

        restTaskTpyeMockMvc.perform(post("/api/task-tpyes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskTpye)))
                .andExpect(status().isCreated());

        // Validate the TaskTpye in the database
        List<TaskTpye> taskTpyes = taskTpyeRepository.findAll();
        assertThat(taskTpyes).hasSize(databaseSizeBeforeCreate + 1);
        TaskTpye testTaskTpye = taskTpyes.get(taskTpyes.size() - 1);
    }

    @Test
    @Transactional
    public void getAllTaskTpyes() throws Exception {
        // Initialize the database
        taskTpyeRepository.saveAndFlush(taskTpye);

        // Get all the taskTpyes
        restTaskTpyeMockMvc.perform(get("/api/task-tpyes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(taskTpye.getId().intValue())));
    }

    @Test
    @Transactional
    public void getTaskTpye() throws Exception {
        // Initialize the database
        taskTpyeRepository.saveAndFlush(taskTpye);

        // Get the taskTpye
        restTaskTpyeMockMvc.perform(get("/api/task-tpyes/{id}", taskTpye.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(taskTpye.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTaskTpye() throws Exception {
        // Get the taskTpye
        restTaskTpyeMockMvc.perform(get("/api/task-tpyes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskTpye() throws Exception {
        // Initialize the database
        taskTpyeRepository.saveAndFlush(taskTpye);
        int databaseSizeBeforeUpdate = taskTpyeRepository.findAll().size();

        // Update the taskTpye
        TaskTpye updatedTaskTpye = new TaskTpye();
        updatedTaskTpye.setId(taskTpye.getId());

        restTaskTpyeMockMvc.perform(put("/api/task-tpyes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTaskTpye)))
                .andExpect(status().isOk());

        // Validate the TaskTpye in the database
        List<TaskTpye> taskTpyes = taskTpyeRepository.findAll();
        assertThat(taskTpyes).hasSize(databaseSizeBeforeUpdate);
        TaskTpye testTaskTpye = taskTpyes.get(taskTpyes.size() - 1);
    }

    @Test
    @Transactional
    public void deleteTaskTpye() throws Exception {
        // Initialize the database
        taskTpyeRepository.saveAndFlush(taskTpye);
        int databaseSizeBeforeDelete = taskTpyeRepository.findAll().size();

        // Get the taskTpye
        restTaskTpyeMockMvc.perform(delete("/api/task-tpyes/{id}", taskTpye.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TaskTpye> taskTpyes = taskTpyeRepository.findAll();
        assertThat(taskTpyes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
