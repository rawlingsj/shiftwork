package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.WeekendDefinition;
import com.teammachine.staffrostering.domain.enumeration.DayOfWeek;
import com.teammachine.staffrostering.repository.WeekendDefinitionRepository;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the WeekendDefinitionResource REST controller.
 *
 * @see WeekendDefinitionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class WeekendDefinitionResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private WeekendDefinitionRepository weekendDefinitionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWeekendDefinitionMockMvc;

    private WeekendDefinition weekendDefinition;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WeekendDefinitionResource weekendDefinitionResource = new WeekendDefinitionResource();
        ReflectionTestUtils.setField(weekendDefinitionResource, "weekendDefinitionRepository", weekendDefinitionRepository);
        this.restWeekendDefinitionMockMvc = MockMvcBuilders.standaloneSetup(weekendDefinitionResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        weekendDefinition = new WeekendDefinition();
        weekendDefinition.setDescription(DEFAULT_DESCRIPTION);
        weekendDefinition.setDays(new LinkedHashSet<>(Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)));
    }

    @Test
    @Transactional
    public void createWeekendDefinition() throws Exception {
        int databaseSizeBeforeCreate = weekendDefinitionRepository.findAll().size();

        // Create the WeekendDefinition
        restWeekendDefinitionMockMvc.perform(post("/api/weekend-definitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(weekendDefinition)))
                .andExpect(status().isCreated());

        // Validate the WeekendDefinition in the database
        List<WeekendDefinition> weekendDefinitions = weekendDefinitionRepository.findAll();
        assertThat(weekendDefinitions).hasSize(databaseSizeBeforeCreate + 1);
        WeekendDefinition testWeekendDefinition = weekendDefinitions.get(weekendDefinitions.size() - 1);
        assertThat(testWeekendDefinition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWeekendDefinition.getDays()).containsExactly(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    }

    @Test
    @Transactional
    public void getAllWeekendDefinitions() throws Exception {
        // Initialize the database
        weekendDefinitionRepository.saveAndFlush(weekendDefinition);

        // Get all the weekendDefinitions
        restWeekendDefinitionMockMvc.perform(get("/api/weekend-definitions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(weekendDefinition.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
                .andExpect(jsonPath("$.[*].days[*]").value(contains(DayOfWeek.SATURDAY.name(), DayOfWeek.SUNDAY.name())));
    }

    @Test
    @Transactional
    public void getWeekendDefinition() throws Exception {
        // Initialize the database
        weekendDefinitionRepository.saveAndFlush(weekendDefinition);

        // Get the weekendDefinition
        restWeekendDefinitionMockMvc.perform(get("/api/weekend-definitions/{id}", weekendDefinition.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(weekendDefinition.getId().intValue()))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.days").value(contains(DayOfWeek.SATURDAY.name(), DayOfWeek.SUNDAY.name())));
    }

    @Test
    @Transactional
    public void getNonExistingWeekendDefinition() throws Exception {
        // Get the weekendDefinition
        restWeekendDefinitionMockMvc.perform(get("/api/weekend-definitions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeekendDefinition() throws Exception {
        // Initialize the database
        weekendDefinitionRepository.saveAndFlush(weekendDefinition);
        int databaseSizeBeforeUpdate = weekendDefinitionRepository.findAll().size();

        // Update the weekendDefinition
        WeekendDefinition updatedWeekendDefinition = new WeekendDefinition();
        updatedWeekendDefinition.setId(weekendDefinition.getId());
        updatedWeekendDefinition.setDescription(UPDATED_DESCRIPTION);
        updatedWeekendDefinition.setDays(new HashSet<>(Arrays.asList(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)));

        restWeekendDefinitionMockMvc.perform(put("/api/weekend-definitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWeekendDefinition)))
                .andExpect(status().isOk());

        // Validate the WeekendDefinition in the database
        List<WeekendDefinition> weekendDefinitions = weekendDefinitionRepository.findAll();
        assertThat(weekendDefinitions).hasSize(databaseSizeBeforeUpdate);
        WeekendDefinition testWeekendDefinition = weekendDefinitions.get(weekendDefinitions.size() - 1);
        assertThat(testWeekendDefinition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWeekendDefinition.getDays()).contains(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
    }

    @Test
    @Transactional
    public void deleteWeekendDefinition() throws Exception {
        // Initialize the database
        weekendDefinitionRepository.saveAndFlush(weekendDefinition);
        int databaseSizeBeforeDelete = weekendDefinitionRepository.findAll().size();

        // Get the weekendDefinition
        restWeekendDefinitionMockMvc.perform(delete("/api/weekend-definitions/{id}", weekendDefinition.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<WeekendDefinition> weekendDefinitions = weekendDefinitionRepository.findAll();
        assertThat(weekendDefinitions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
