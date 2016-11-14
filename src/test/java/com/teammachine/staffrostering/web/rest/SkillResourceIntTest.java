package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.Skill;
import com.teammachine.staffrostering.domain.enumeration.DurationUnit;
import com.teammachine.staffrostering.repository.SkillRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Period;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SkillResource REST controller.
 *
 * @see SkillResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class SkillResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final int DEFAULT_ROTATION_PERIOD_VALUE = 2;
    private static final DurationUnit DEFAULT_ROTATION_PERIOD_UNIT = DurationUnit.MONTHS;
    private static final Period DEFAULT_ROTATION_PERIOD = Period.ofMonths(DEFAULT_ROTATION_PERIOD_VALUE);

    private static final String UPDATED_CODE = "BBBBB";
    private static final int UPDATED_ROTATION_PERIOD_VALUE = 20;
    private static final DurationUnit UPDATED_ROTATION_PERIOD_UNIT = DurationUnit.DAYS;
    private static final Period UPDATED_ROTATION_PERIOD = Period.ofDays(UPDATED_ROTATION_PERIOD_VALUE);

    @Inject
    private SkillRepository skillRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSkillMockMvc;

    private Skill skill;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SkillResource skillResource = new SkillResource();
        ReflectionTestUtils.setField(skillResource, "skillRepository", skillRepository);
        this.restSkillMockMvc = MockMvcBuilders.standaloneSetup(skillResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        skill = new Skill();
        skill.setCode(DEFAULT_CODE);
        skill.setRotationPeriodValue(DEFAULT_ROTATION_PERIOD_VALUE);
        skill.setRotationPeriodUnit(DEFAULT_ROTATION_PERIOD_UNIT);
    }

    @Test
    @Transactional
    public void createSkill() throws Exception {
        int databaseSizeBeforeCreate = skillRepository.findAll().size();

        // Create the Skill
        restSkillMockMvc.perform(post("/api/skills")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(skill)))
                .andExpect(status().isCreated());

        // Validate the Skill in the database
        List<Skill> skills = skillRepository.findAll();
        assertThat(skills).hasSize(databaseSizeBeforeCreate + 1);
        Skill testSkill = skills.get(skills.size() - 1);
        assertThat(testSkill.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSkill.getRotationPeriodValue()).isEqualTo(DEFAULT_ROTATION_PERIOD_VALUE);
        assertThat(testSkill.getRotationPeriodUnit()).isEqualTo(DEFAULT_ROTATION_PERIOD_UNIT);
        assertThat(testSkill.getRotationPeriod()).isEqualTo(DEFAULT_ROTATION_PERIOD);
    }

    @Test
    @Transactional
    public void getAllSkills() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skills
        restSkillMockMvc.perform(get("/api/skills?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(skill.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
                .andExpect(jsonPath("$.[*].rotationPeriodValue").value(hasItem(DEFAULT_ROTATION_PERIOD_VALUE)))
                .andExpect(jsonPath("$.[*].rotationPeriodUnit").value(hasItem(DEFAULT_ROTATION_PERIOD_UNIT.toString())))
                .andExpect(jsonPath("$.[*].rotationPeriod").value(hasItem(DEFAULT_ROTATION_PERIOD.toString())));
    }

    @Test
    @Transactional
    public void getSkill() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get the skill
        restSkillMockMvc.perform(get("/api/skills/{id}", skill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(skill.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.rotationPeriodValue").value(DEFAULT_ROTATION_PERIOD_VALUE))
            .andExpect(jsonPath("$.rotationPeriodUnit").value(DEFAULT_ROTATION_PERIOD_UNIT.toString()))
            .andExpect(jsonPath("$.rotationPeriod").value(DEFAULT_ROTATION_PERIOD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSkill() throws Exception {
        // Get the skill
        restSkillMockMvc.perform(get("/api/skills/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSkill() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);
        int databaseSizeBeforeUpdate = skillRepository.findAll().size();

        // Update the skill
        Skill updatedSkill = new Skill();
        updatedSkill.setId(skill.getId());
        updatedSkill.setCode(UPDATED_CODE);
        updatedSkill.setRotationPeriodValue(UPDATED_ROTATION_PERIOD_VALUE);
        updatedSkill.setRotationPeriodUnit(UPDATED_ROTATION_PERIOD_UNIT);

        restSkillMockMvc.perform(put("/api/skills")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSkill)))
                .andExpect(status().isOk());

        // Validate the Skill in the database
        List<Skill> skills = skillRepository.findAll();
        assertThat(skills).hasSize(databaseSizeBeforeUpdate);
        Skill testSkill = skills.get(skills.size() - 1);
        assertThat(testSkill.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSkill.getRotationPeriodValue()).isEqualTo(UPDATED_ROTATION_PERIOD_VALUE);
        assertThat(testSkill.getRotationPeriodUnit()).isEqualTo(UPDATED_ROTATION_PERIOD_UNIT);
        assertThat(testSkill.getRotationPeriod()).isEqualTo(UPDATED_ROTATION_PERIOD);
    }

    @Test
    @Transactional
    public void deleteSkill() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);
        int databaseSizeBeforeDelete = skillRepository.findAll().size();

        // Get the skill
        restSkillMockMvc.perform(delete("/api/skills/{id}", skill.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Skill> skills = skillRepository.findAll();
        assertThat(skills).hasSize(databaseSizeBeforeDelete - 1);
    }
}
