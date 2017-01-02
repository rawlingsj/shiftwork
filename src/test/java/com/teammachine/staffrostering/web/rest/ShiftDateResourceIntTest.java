package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.repository.ShiftDateRepository;

import org.junit.Before;
import org.junit.Ignore;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.teammachine.staffrostering.domain.enumeration.DayOfWeek;

/**
 * Test class for the ShiftDateResource REST controller.
 *
 * @see ShiftDateResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
@Ignore
public class ShiftDateResourceIntTest {


    private static final Integer DEFAULT_DAY_INDEX = 1;
    private static final Integer UPDATED_DAY_INDEX = 2;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final DayOfWeek DEFAULT_DAY_OF_WEEK = DayOfWeek.MONDAY;
    private static final DayOfWeek UPDATED_DAY_OF_WEEK = DayOfWeek.TUESDAY;

    @Inject
    private ShiftDateRepository shiftDateRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restShiftDateMockMvc;

    private ShiftDate shiftDate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShiftDateResource shiftDateResource = new ShiftDateResource();
        ReflectionTestUtils.setField(shiftDateResource, "shiftDateRepository", shiftDateRepository);
        this.restShiftDateMockMvc = MockMvcBuilders.standaloneSetup(shiftDateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        shiftDate = new ShiftDate();
        shiftDate.setDayIndex(DEFAULT_DAY_INDEX);
        shiftDate.setDate(DEFAULT_DATE);
        shiftDate.setDayOfWeek(DEFAULT_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void createShiftDate() throws Exception {
        int databaseSizeBeforeCreate = shiftDateRepository.findAll().size();

        // Create the ShiftDate

        restShiftDateMockMvc.perform(post("/api/shift-dates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shiftDate)))
                .andExpect(status().isCreated());

        // Validate the ShiftDate in the database
        List<ShiftDate> shiftDates = shiftDateRepository.findAll();
        assertThat(shiftDates).hasSize(databaseSizeBeforeCreate + 1);
        ShiftDate testShiftDate = shiftDates.get(shiftDates.size() - 1);
        assertThat(testShiftDate.getDayIndex()).isEqualTo(DEFAULT_DAY_INDEX);
        assertThat(testShiftDate.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testShiftDate.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void getAllShiftDates() throws Exception {
        // Initialize the database
        shiftDateRepository.saveAndFlush(shiftDate);

        // Get all the shiftDates
        restShiftDateMockMvc.perform(get("/api/shift-dates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(shiftDate.getId().intValue())))
                .andExpect(jsonPath("$.[*].dayIndex").value(hasItem(DEFAULT_DAY_INDEX)))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK.toString())));
    }

    @Test
    @Transactional
    public void getShiftDate() throws Exception {
        // Initialize the database
        shiftDateRepository.saveAndFlush(shiftDate);

        // Get the shiftDate
        restShiftDateMockMvc.perform(get("/api/shift-dates/{id}", shiftDate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(shiftDate.getId().intValue()))
            .andExpect(jsonPath("$.dayIndex").value(DEFAULT_DAY_INDEX))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShiftDate() throws Exception {
        // Get the shiftDate
        restShiftDateMockMvc.perform(get("/api/shift-dates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShiftDate() throws Exception {
        // Initialize the database
        shiftDateRepository.saveAndFlush(shiftDate);
        int databaseSizeBeforeUpdate = shiftDateRepository.findAll().size();

        // Update the shiftDate
        ShiftDate updatedShiftDate = new ShiftDate();
        updatedShiftDate.setId(shiftDate.getId());
        updatedShiftDate.setDayIndex(UPDATED_DAY_INDEX);
        updatedShiftDate.setDate(UPDATED_DATE);
        updatedShiftDate.setDayOfWeek(UPDATED_DAY_OF_WEEK);

        restShiftDateMockMvc.perform(put("/api/shift-dates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedShiftDate)))
                .andExpect(status().isOk());

        // Validate the ShiftDate in the database
        List<ShiftDate> shiftDates = shiftDateRepository.findAll();
        assertThat(shiftDates).hasSize(databaseSizeBeforeUpdate);
        ShiftDate testShiftDate = shiftDates.get(shiftDates.size() - 1);
        assertThat(testShiftDate.getDayIndex()).isEqualTo(UPDATED_DAY_INDEX);
        assertThat(testShiftDate.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testShiftDate.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void deleteShiftDate() throws Exception {
        // Initialize the database
        shiftDateRepository.saveAndFlush(shiftDate);
        int databaseSizeBeforeDelete = shiftDateRepository.findAll().size();

        // Get the shiftDate
        restShiftDateMockMvc.perform(delete("/api/shift-dates/{id}", shiftDate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ShiftDate> shiftDates = shiftDateRepository.findAll();
        assertThat(shiftDates).hasSize(databaseSizeBeforeDelete - 1);
    }
}
