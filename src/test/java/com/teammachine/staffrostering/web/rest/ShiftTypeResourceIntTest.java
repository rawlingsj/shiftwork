package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.ShiftType;
import com.teammachine.staffrostering.repository.ShiftTypeRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ShiftTypeResource REST controller.
 *
 * @see ShiftTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class ShiftTypeResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Boolean DEFAULT_NIGHT_SHIFT = false;
    private static final Boolean UPDATED_NIGHT_SHIFT = true;

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_TIME_STR = dateTimeFormatter.format(DEFAULT_START_TIME);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_TIME_STR = dateTimeFormatter.format(DEFAULT_END_TIME);

    @Inject
    private ShiftTypeRepository shiftTypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restShiftTypeMockMvc;

    private ShiftType shiftType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShiftTypeResource shiftTypeResource = new ShiftTypeResource();
        ReflectionTestUtils.setField(shiftTypeResource, "shiftTypeRepository", shiftTypeRepository);
        this.restShiftTypeMockMvc = MockMvcBuilders.standaloneSetup(shiftTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        shiftType = new ShiftType();
        shiftType.setCode(DEFAULT_CODE);
        shiftType.setDescription(DEFAULT_DESCRIPTION);
        shiftType.setNightShift(DEFAULT_NIGHT_SHIFT);
        shiftType.setStartTime(DEFAULT_START_TIME);
        shiftType.setEndTime(DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    public void createShiftType() throws Exception {
        int databaseSizeBeforeCreate = shiftTypeRepository.findAll().size();

        // Create the ShiftType

        restShiftTypeMockMvc.perform(post("/api/shift-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shiftType)))
                .andExpect(status().isCreated());

        // Validate the ShiftType in the database
        List<ShiftType> shiftTypes = shiftTypeRepository.findAll();
        assertThat(shiftTypes).hasSize(databaseSizeBeforeCreate + 1);
        ShiftType testShiftType = shiftTypes.get(shiftTypes.size() - 1);
        assertThat(testShiftType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testShiftType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testShiftType.isNightShift()).isEqualTo(DEFAULT_NIGHT_SHIFT);
        assertThat(testShiftType.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testShiftType.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    public void getAllShiftTypes() throws Exception {
        // Initialize the database
        shiftTypeRepository.saveAndFlush(shiftType);

        // Get all the shiftTypes
        restShiftTypeMockMvc.perform(get("/api/shift-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(shiftType.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].nightShift").value(hasItem(DEFAULT_NIGHT_SHIFT.booleanValue())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)));
    }

    @Test
    @Transactional
    public void getShiftType() throws Exception {
        // Initialize the database
        shiftTypeRepository.saveAndFlush(shiftType);

        // Get the shiftType
        restShiftTypeMockMvc.perform(get("/api/shift-types/{id}", shiftType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(shiftType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.nightShift").value(DEFAULT_NIGHT_SHIFT.booleanValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME_STR))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingShiftType() throws Exception {
        // Get the shiftType
        restShiftTypeMockMvc.perform(get("/api/shift-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShiftType() throws Exception {
        // Initialize the database
        shiftTypeRepository.saveAndFlush(shiftType);
        int databaseSizeBeforeUpdate = shiftTypeRepository.findAll().size();

        // Update the shiftType
        ShiftType updatedShiftType = new ShiftType();
        updatedShiftType.setId(shiftType.getId());
        updatedShiftType.setCode(UPDATED_CODE);
        updatedShiftType.setDescription(UPDATED_DESCRIPTION);
        updatedShiftType.setNightShift(UPDATED_NIGHT_SHIFT);
        updatedShiftType.setStartTime(UPDATED_START_TIME);
        updatedShiftType.setEndTime(UPDATED_END_TIME);

        restShiftTypeMockMvc.perform(put("/api/shift-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedShiftType)))
                .andExpect(status().isOk());

        // Validate the ShiftType in the database
        List<ShiftType> shiftTypes = shiftTypeRepository.findAll();
        assertThat(shiftTypes).hasSize(databaseSizeBeforeUpdate);
        ShiftType testShiftType = shiftTypes.get(shiftTypes.size() - 1);
        assertThat(testShiftType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testShiftType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testShiftType.isNightShift()).isEqualTo(UPDATED_NIGHT_SHIFT);
        assertThat(testShiftType.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testShiftType.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void deleteShiftType() throws Exception {
        // Initialize the database
        shiftTypeRepository.saveAndFlush(shiftType);
        int databaseSizeBeforeDelete = shiftTypeRepository.findAll().size();

        // Get the shiftType
        restShiftTypeMockMvc.perform(delete("/api/shift-types/{id}", shiftType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ShiftType> shiftTypes = shiftTypeRepository.findAll();
        assertThat(shiftTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
