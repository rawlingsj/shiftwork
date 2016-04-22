package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.WeekendDay;
import com.teammachine.staffrostering.repository.WeekendDayRepository;

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

import com.teammachine.staffrostering.domain.enumeration.DayOfWeek;

/**
 * Test class for the WeekendDayResource REST controller.
 *
 * @see WeekendDayResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class WeekendDayResourceIntTest {


    private static final DayOfWeek DEFAULT_DAY_OF_WEEK = DayOfWeek.MONDAY;
    private static final DayOfWeek UPDATED_DAY_OF_WEEK = DayOfWeek.TUESDAY;

    @Inject
    private WeekendDayRepository weekendDayRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWeekendDayMockMvc;

    private WeekendDay weekendDay;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WeekendDayResource weekendDayResource = new WeekendDayResource();
        ReflectionTestUtils.setField(weekendDayResource, "weekendDayRepository", weekendDayRepository);
        this.restWeekendDayMockMvc = MockMvcBuilders.standaloneSetup(weekendDayResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        weekendDay = new WeekendDay();
        weekendDay.setDayOfWeek(DEFAULT_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void createWeekendDay() throws Exception {
        int databaseSizeBeforeCreate = weekendDayRepository.findAll().size();

        // Create the WeekendDay

        restWeekendDayMockMvc.perform(post("/api/weekend-days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(weekendDay)))
                .andExpect(status().isCreated());

        // Validate the WeekendDay in the database
        List<WeekendDay> weekendDays = weekendDayRepository.findAll();
        assertThat(weekendDays).hasSize(databaseSizeBeforeCreate + 1);
        WeekendDay testWeekendDay = weekendDays.get(weekendDays.size() - 1);
        assertThat(testWeekendDay.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void getAllWeekendDays() throws Exception {
        // Initialize the database
        weekendDayRepository.saveAndFlush(weekendDay);

        // Get all the weekendDays
        restWeekendDayMockMvc.perform(get("/api/weekend-days?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(weekendDay.getId().intValue())))
                .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK.toString())));
    }

    @Test
    @Transactional
    public void getWeekendDay() throws Exception {
        // Initialize the database
        weekendDayRepository.saveAndFlush(weekendDay);

        // Get the weekendDay
        restWeekendDayMockMvc.perform(get("/api/weekend-days/{id}", weekendDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(weekendDay.getId().intValue()))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWeekendDay() throws Exception {
        // Get the weekendDay
        restWeekendDayMockMvc.perform(get("/api/weekend-days/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeekendDay() throws Exception {
        // Initialize the database
        weekendDayRepository.saveAndFlush(weekendDay);
        int databaseSizeBeforeUpdate = weekendDayRepository.findAll().size();

        // Update the weekendDay
        WeekendDay updatedWeekendDay = new WeekendDay();
        updatedWeekendDay.setId(weekendDay.getId());
        updatedWeekendDay.setDayOfWeek(UPDATED_DAY_OF_WEEK);

        restWeekendDayMockMvc.perform(put("/api/weekend-days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWeekendDay)))
                .andExpect(status().isOk());

        // Validate the WeekendDay in the database
        List<WeekendDay> weekendDays = weekendDayRepository.findAll();
        assertThat(weekendDays).hasSize(databaseSizeBeforeUpdate);
        WeekendDay testWeekendDay = weekendDays.get(weekendDays.size() - 1);
        assertThat(testWeekendDay.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void deleteWeekendDay() throws Exception {
        // Initialize the database
        weekendDayRepository.saveAndFlush(weekendDay);
        int databaseSizeBeforeDelete = weekendDayRepository.findAll().size();

        // Get the weekendDay
        restWeekendDayMockMvc.perform(delete("/api/weekend-days/{id}", weekendDay.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<WeekendDay> weekendDays = weekendDayRepository.findAll();
        assertThat(weekendDays).hasSize(databaseSizeBeforeDelete - 1);
    }
}
