package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.TaskSkillRequirement;
import com.teammachine.staffrostering.repository.TaskSkillRequirementRepository;

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
 * Test class for the TaskSkillRequirementResource REST controller.
 *
 * @see TaskSkillRequirementResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class TaskSkillRequirementResourceIntTest {


    @Inject
    private TaskSkillRequirementRepository taskSkillRequirementRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaskSkillRequirementMockMvc;

    private TaskSkillRequirement taskSkillRequirement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskSkillRequirementResource taskSkillRequirementResource = new TaskSkillRequirementResource();
        ReflectionTestUtils.setField(taskSkillRequirementResource, "taskSkillRequirementRepository", taskSkillRequirementRepository);
        this.restTaskSkillRequirementMockMvc = MockMvcBuilders.standaloneSetup(taskSkillRequirementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        taskSkillRequirement = new TaskSkillRequirement();
    }

    @Test
    @Transactional
    public void createTaskSkillRequirement() throws Exception {
        int databaseSizeBeforeCreate = taskSkillRequirementRepository.findAll().size();

        // Create the TaskSkillRequirement

        restTaskSkillRequirementMockMvc.perform(post("/api/task-skill-requirements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskSkillRequirement)))
                .andExpect(status().isCreated());

        // Validate the TaskSkillRequirement in the database
        List<TaskSkillRequirement> taskSkillRequirements = taskSkillRequirementRepository.findAll();
        assertThat(taskSkillRequirements).hasSize(databaseSizeBeforeCreate + 1);
        TaskSkillRequirement testTaskSkillRequirement = taskSkillRequirements.get(taskSkillRequirements.size() - 1);
    }

    @Test
    @Transactional
    public void getAllTaskSkillRequirements() throws Exception {
        // Initialize the database
        taskSkillRequirementRepository.saveAndFlush(taskSkillRequirement);

        // Get all the taskSkillRequirements
        restTaskSkillRequirementMockMvc.perform(get("/api/task-skill-requirements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(taskSkillRequirement.getId().intValue())));
    }

    @Test
    @Transactional
    public void getTaskSkillRequirement() throws Exception {
        // Initialize the database
        taskSkillRequirementRepository.saveAndFlush(taskSkillRequirement);

        // Get the taskSkillRequirement
        restTaskSkillRequirementMockMvc.perform(get("/api/task-skill-requirements/{id}", taskSkillRequirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(taskSkillRequirement.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTaskSkillRequirement() throws Exception {
        // Get the taskSkillRequirement
        restTaskSkillRequirementMockMvc.perform(get("/api/task-skill-requirements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskSkillRequirement() throws Exception {
        // Initialize the database
        taskSkillRequirementRepository.saveAndFlush(taskSkillRequirement);
        int databaseSizeBeforeUpdate = taskSkillRequirementRepository.findAll().size();

        // Update the taskSkillRequirement
        TaskSkillRequirement updatedTaskSkillRequirement = new TaskSkillRequirement();
        updatedTaskSkillRequirement.setId(taskSkillRequirement.getId());

        restTaskSkillRequirementMockMvc.perform(put("/api/task-skill-requirements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTaskSkillRequirement)))
                .andExpect(status().isOk());

        // Validate the TaskSkillRequirement in the database
        List<TaskSkillRequirement> taskSkillRequirements = taskSkillRequirementRepository.findAll();
        assertThat(taskSkillRequirements).hasSize(databaseSizeBeforeUpdate);
        TaskSkillRequirement testTaskSkillRequirement = taskSkillRequirements.get(taskSkillRequirements.size() - 1);
    }

    @Test
    @Transactional
    public void deleteTaskSkillRequirement() throws Exception {
        // Initialize the database
        taskSkillRequirementRepository.saveAndFlush(taskSkillRequirement);
        int databaseSizeBeforeDelete = taskSkillRequirementRepository.findAll().size();

        // Get the taskSkillRequirement
        restTaskSkillRequirementMockMvc.perform(delete("/api/task-skill-requirements/{id}", taskSkillRequirement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TaskSkillRequirement> taskSkillRequirements = taskSkillRequirementRepository.findAll();
        assertThat(taskSkillRequirements).hasSize(databaseSizeBeforeDelete - 1);
    }
}
