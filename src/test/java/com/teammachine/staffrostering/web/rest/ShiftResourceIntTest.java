package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.Shift;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;
import com.teammachine.staffrostering.repository.ShiftRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ShiftResource REST controller.
 *
 * @see ShiftResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class ShiftResourceIntTest {


    private static final Integer DEFAULT_INDEX = 1;
    private static final Integer UPDATED_INDEX = 2;

    private static final Integer DEFAULT_STAFF_REQUIRED = 1;
    private static final Integer UPDATED_STAFF_REQUIRED = 2;

    @Inject
    private ShiftRepository shiftRepository;

    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restShiftMockMvc;

    private Shift shift;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShiftResource shiftResource = new ShiftResource();
        ReflectionTestUtils.setField(shiftResource, "shiftRepository", shiftRepository);
        ReflectionTestUtils.setField(shiftResource, "shiftAssignmentRepository", shiftAssignmentRepository);
        this.restShiftMockMvc = MockMvcBuilders.standaloneSetup(shiftResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        shift = new Shift();
        shift.setIndex(DEFAULT_INDEX);
        shift.setStaffRequired(DEFAULT_STAFF_REQUIRED);
    }

    @Test
    @Transactional
    public void createShift() throws Exception {
        int databaseSizeBeforeCreate = shiftRepository.findAll().size();

        // Create the Shift

        restShiftMockMvc.perform(post("/api/shifts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shift)))
                .andExpect(status().isCreated());

        // Validate the Shift in the database
        List<Shift> shifts = shiftRepository.findAll();
        assertThat(shifts).hasSize(databaseSizeBeforeCreate + 1);
        Shift testShift = shifts.get(shifts.size() - 1);
        assertThat(testShift.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testShift.getStaffRequired()).isEqualTo(DEFAULT_STAFF_REQUIRED);
    }

    @Test
    @Transactional
    public void getAllShifts() throws Exception {
        // Initialize the database
        shiftRepository.saveAndFlush(shift);

        // Get all the shifts
        restShiftMockMvc.perform(get("/api/shifts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(shift.getId().intValue())))
                .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
                .andExpect(jsonPath("$.[*].staffRequired").value(hasItem(DEFAULT_STAFF_REQUIRED)));
    }

    @Test
    @Transactional
    public void getShift() throws Exception {
        // Initialize the database
        shiftRepository.saveAndFlush(shift);

        // Get the shift
        restShiftMockMvc.perform(get("/api/shifts/{id}", shift.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(shift.getId().intValue()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX))
            .andExpect(jsonPath("$.staffRequired").value(DEFAULT_STAFF_REQUIRED));
    }

    @Test
    @Transactional
    public void getNonExistingShift() throws Exception {
        // Get the shift
        restShiftMockMvc.perform(get("/api/shifts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShift() throws Exception {
        // Initialize the database
        shiftRepository.saveAndFlush(shift);
        int databaseSizeBeforeUpdate = shiftRepository.findAll().size();

        // Update the shift
        Shift updatedShift = new Shift();
        updatedShift.setId(shift.getId());
        updatedShift.setIndex(UPDATED_INDEX);
        updatedShift.setStaffRequired(UPDATED_STAFF_REQUIRED);

        restShiftMockMvc.perform(put("/api/shifts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedShift)))
                .andExpect(status().isOk());

        // Validate the Shift in the database
        List<Shift> shifts = shiftRepository.findAll();
        assertThat(shifts).hasSize(databaseSizeBeforeUpdate);
        Shift testShift = shifts.get(shifts.size() - 1);
        assertThat(testShift.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testShift.getStaffRequired()).isEqualTo(UPDATED_STAFF_REQUIRED);
    }

    @Test
    @Transactional
    public void deleteShift() throws Exception {
        // Initialize the database
        shiftRepository.saveAndFlush(shift);
        int databaseSizeBeforeDelete = shiftRepository.findAll().size();

        // Get the shift
        restShiftMockMvc.perform(delete("/api/shifts/{id}", shift.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Shift> shifts = shiftRepository.findAll();
        assertThat(shifts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
