package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.SkillProficiency;
import com.teammachine.staffrostering.repository.SkillProficiencyRepository;

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
 * Test class for the SkillProficiencyResource REST controller.
 *
 * @see SkillProficiencyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class SkillProficiencyResourceIntTest {


    @Inject
    private SkillProficiencyRepository skillProficiencyRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSkillProficiencyMockMvc;

    private SkillProficiency skillProficiency;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SkillProficiencyResource skillProficiencyResource = new SkillProficiencyResource();
        ReflectionTestUtils.setField(skillProficiencyResource, "skillProficiencyRepository", skillProficiencyRepository);
        this.restSkillProficiencyMockMvc = MockMvcBuilders.standaloneSetup(skillProficiencyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        skillProficiency = new SkillProficiency();
    }

    @Test
    @Transactional
    public void createSkillProficiency() throws Exception {
        int databaseSizeBeforeCreate = skillProficiencyRepository.findAll().size();

        // Create the SkillProficiency

        restSkillProficiencyMockMvc.perform(post("/api/skill-proficiencies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(skillProficiency)))
                .andExpect(status().isCreated());

        // Validate the SkillProficiency in the database
        List<SkillProficiency> skillProficiencies = skillProficiencyRepository.findAll();
        assertThat(skillProficiencies).hasSize(databaseSizeBeforeCreate + 1);
        SkillProficiency testSkillProficiency = skillProficiencies.get(skillProficiencies.size() - 1);
    }

    @Test
    @Transactional
    public void getAllSkillProficiencies() throws Exception {
        // Initialize the database
        skillProficiencyRepository.saveAndFlush(skillProficiency);

        // Get all the skillProficiencies
        restSkillProficiencyMockMvc.perform(get("/api/skill-proficiencies?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(skillProficiency.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSkillProficiency() throws Exception {
        // Initialize the database
        skillProficiencyRepository.saveAndFlush(skillProficiency);

        // Get the skillProficiency
        restSkillProficiencyMockMvc.perform(get("/api/skill-proficiencies/{id}", skillProficiency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(skillProficiency.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSkillProficiency() throws Exception {
        // Get the skillProficiency
        restSkillProficiencyMockMvc.perform(get("/api/skill-proficiencies/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSkillProficiency() throws Exception {
        // Initialize the database
        skillProficiencyRepository.saveAndFlush(skillProficiency);
        int databaseSizeBeforeUpdate = skillProficiencyRepository.findAll().size();

        // Update the skillProficiency
        SkillProficiency updatedSkillProficiency = new SkillProficiency();
        updatedSkillProficiency.setId(skillProficiency.getId());

        restSkillProficiencyMockMvc.perform(put("/api/skill-proficiencies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSkillProficiency)))
                .andExpect(status().isOk());

        // Validate the SkillProficiency in the database
        List<SkillProficiency> skillProficiencies = skillProficiencyRepository.findAll();
        assertThat(skillProficiencies).hasSize(databaseSizeBeforeUpdate);
        SkillProficiency testSkillProficiency = skillProficiencies.get(skillProficiencies.size() - 1);
    }

    @Test
    @Transactional
    public void deleteSkillProficiency() throws Exception {
        // Initialize the database
        skillProficiencyRepository.saveAndFlush(skillProficiency);
        int databaseSizeBeforeDelete = skillProficiencyRepository.findAll().size();

        // Get the skillProficiency
        restSkillProficiencyMockMvc.perform(delete("/api/skill-proficiencies/{id}", skillProficiency.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SkillProficiency> skillProficiencies = skillProficiencyRepository.findAll();
        assertThat(skillProficiencies).hasSize(databaseSizeBeforeDelete - 1);
    }
}
