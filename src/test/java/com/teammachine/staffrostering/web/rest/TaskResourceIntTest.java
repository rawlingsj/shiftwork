package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.Task;
import com.teammachine.staffrostering.repository.TaskRepository;

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

import com.teammachine.staffrostering.domain.enumeration.TaskType;
import com.teammachine.staffrostering.domain.enumeration.TaskImportance;
import com.teammachine.staffrostering.domain.enumeration.TaskUrgency;

/**
 * Test class for the TaskResource REST controller.
 *
 * @see TaskResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class TaskResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_STAFF_NEEDED = 1;
    private static final Integer UPDATED_STAFF_NEEDED = 2;

    private static final TaskType DEFAULT_TASK_TYPE = TaskType.SHORT;
    private static final TaskType UPDATED_TASK_TYPE = TaskType.FULL;

    private static final TaskImportance DEFAULT_IMPORTANCE = TaskImportance.IMPORTANT;
    private static final TaskImportance UPDATED_IMPORTANCE = TaskImportance.NOT_IMPORTANT;

    private static final TaskUrgency DEFAULT_URGENCY = TaskUrgency.URGENT;
    private static final TaskUrgency UPDATED_URGENCY = TaskUrgency.NOT_URGENT;

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaskMockMvc;

    private Task task;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskResource taskResource = new TaskResource();
        ReflectionTestUtils.setField(taskResource, "taskRepository", taskRepository);
        this.restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        task = new Task();
        task.setCode(DEFAULT_CODE);
        task.setDescription(DEFAULT_DESCRIPTION);
        task.setStaffNeeded(DEFAULT_STAFF_NEEDED);
        task.setTaskType(DEFAULT_TASK_TYPE);
        task.setImportance(DEFAULT_IMPORTANCE);
        task.setUrgency(DEFAULT_URGENCY);
    }

    @Test
    @Transactional
    public void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // Create the Task

        restTaskMockMvc.perform(post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task)))
                .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = tasks.get(tasks.size() - 1);
        assertThat(testTask.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTask.getStaffNeeded()).isEqualTo(DEFAULT_STAFF_NEEDED);
        assertThat(testTask.getTaskType()).isEqualTo(DEFAULT_TASK_TYPE);
        assertThat(testTask.getImportance()).isEqualTo(DEFAULT_IMPORTANCE);
        assertThat(testTask.getUrgency()).isEqualTo(DEFAULT_URGENCY);
    }

    @Test
    @Transactional
    public void getAllTasks() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the tasks
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].staffNeeded").value(hasItem(DEFAULT_STAFF_NEEDED)))
                .andExpect(jsonPath("$.[*].taskType").value(hasItem(DEFAULT_TASK_TYPE.toString())))
                .andExpect(jsonPath("$.[*].importance").value(hasItem(DEFAULT_IMPORTANCE.getCode())))
                .andExpect(jsonPath("$.[*].urgency").value(hasItem(DEFAULT_URGENCY.getCode())));
    }

    @Test
    @Transactional
    public void getTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.staffNeeded").value(DEFAULT_STAFF_NEEDED))
            .andExpect(jsonPath("$.taskType").value(DEFAULT_TASK_TYPE.toString()))
            .andExpect(jsonPath("$.importance").value(DEFAULT_IMPORTANCE.getCode()))
            .andExpect(jsonPath("$.urgency").value(DEFAULT_URGENCY.getCode()));
    }

    @Test
    @Transactional
    public void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task
        Task updatedTask = new Task();
        updatedTask.setId(task.getId());
        updatedTask.setCode(UPDATED_CODE);
        updatedTask.setDescription(UPDATED_DESCRIPTION);
        updatedTask.setStaffNeeded(UPDATED_STAFF_NEEDED);
        updatedTask.setTaskType(UPDATED_TASK_TYPE);
        updatedTask.setImportance(UPDATED_IMPORTANCE);
        updatedTask.setUrgency(UPDATED_URGENCY);

        restTaskMockMvc.perform(put("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTask)))
                .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeUpdate);
        Task testTask = tasks.get(tasks.size() - 1);
        assertThat(testTask.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTask.getStaffNeeded()).isEqualTo(UPDATED_STAFF_NEEDED);
        assertThat(testTask.getTaskType()).isEqualTo(UPDATED_TASK_TYPE);
        assertThat(testTask.getImportance()).isEqualTo(UPDATED_IMPORTANCE);
        assertThat(testTask.getUrgency()).isEqualTo(UPDATED_URGENCY);
    }

    @Test
    @Transactional
    public void deleteTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        int databaseSizeBeforeDelete = taskRepository.findAll().size();

        // Get the task
        restTaskMockMvc.perform(delete("/api/tasks/{id}", task.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
