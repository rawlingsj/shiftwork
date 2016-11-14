package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.Shift;
import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.domain.ShiftType;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;
import com.teammachine.staffrostering.repository.ShiftDateRepository;
import com.teammachine.staffrostering.repository.ShiftRepository;
import com.teammachine.staffrostering.repository.ShiftTypeRepository;
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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ShiftAssignmentResource REST controller.
 *
 * @see ShiftAssignmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class ShiftAssignmentResourceIntTest {

    private static final Integer DEFAULT_INDEX_IN_SHIFT = 1;
    private static final Integer UPDATED_INDEX_IN_SHIFT = 2;

    private static final Boolean DEFAULT_IS_NEED_TO_REASSIGN = false;
    private static final Boolean UPDATED_IS_NEED_TO_REASSIGN = true;

    private static final Boolean DEFAULT_IS_DROPPED = false;
    private static final Boolean UPDATED_IS_DROPPED = true;

    private static final Boolean DEFAULT_LOCKED = false;
    private static final Boolean UPDATED_LOCKED = true;

    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;

    @Inject
    private ShiftRepository shiftRepository;

    @Inject
    private ShiftDateRepository shiftDateRepository;

    @Inject
    private ShiftTypeRepository shiftTypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restShiftAssignmentMockMvc;

    private ShiftAssignment shiftAssignment;
    private ShiftDate shiftDate;
    private ShiftType shiftType_E, shiftType_L;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShiftAssignmentResource shiftAssignmentResource = new ShiftAssignmentResource();
        ReflectionTestUtils.setField(shiftAssignmentResource, "shiftAssignmentRepository", shiftAssignmentRepository);
        ReflectionTestUtils.setField(shiftAssignmentResource, "shiftDateRepository", shiftDateRepository);
        this.restShiftAssignmentMockMvc = MockMvcBuilders.standaloneSetup(shiftAssignmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        shiftDate = createShiftDate(1);
        shiftType_E = createShiftType("E", 1);
        shiftType_L = createShiftType("L", 2);
        Shift shift = createShift(shiftDate, shiftType_E);

        shiftAssignment = new ShiftAssignment();
        shiftAssignment.setIndexInShift(DEFAULT_INDEX_IN_SHIFT);
        shiftAssignment.setIsNeedToReassign(DEFAULT_IS_NEED_TO_REASSIGN);
        shiftAssignment.setIsDropped(DEFAULT_IS_DROPPED);
        shiftAssignment.setLocked(DEFAULT_LOCKED);
        shiftAssignment.setShift(shift);
    }

    private ShiftType createShiftType(String code, int index) {
        ShiftType shiftType = new ShiftType();
        shiftType.setCode(code);
        shiftType.setIndex(index);
        shiftTypeRepository.saveAndFlush(shiftType);
        return shiftType;
    }

    private ShiftDate createShiftDate(int index) {
        ShiftDate shiftDate = new ShiftDate();
        shiftDate.setDayIndex(index);
        shiftDateRepository.saveAndFlush(shiftDate);
        return shiftDate;
    }

    private Shift createShift(ShiftDate shiftDate, ShiftType shiftType) {
        Shift shift = new Shift();
        shift.setShiftDate(shiftDate);
        shift.setShiftType(shiftType);
        shiftRepository.saveAndFlush(shift);
        return shift;
    }

    private ShiftAssignment createShiftAssignment(Shift shift, int indexInShift) {
        ShiftAssignment shiftAssignment = new ShiftAssignment();
        shiftAssignment.setShift(shift);
        shiftAssignment.setIndexInShift(indexInShift);
        shiftAssignmentRepository.saveAndFlush(shiftAssignment);
        return shiftAssignment;
    }

    @Test
    public void createShiftAssignment() throws Exception {
        int databaseSizeBeforeCreate = shiftAssignmentRepository.findAll().size();

        // Create the ShiftAssignment
        restShiftAssignmentMockMvc.perform(post("/api/shift-assignments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shiftAssignment)))
                .andExpect(status().isCreated());

        // Validate the ShiftAssignment in the database
        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findAll();
        assertThat(shiftAssignments).hasSize(databaseSizeBeforeCreate + 1);
        ShiftAssignment testShiftAssignment = shiftAssignments.get(shiftAssignments.size() - 1);
        assertThat(testShiftAssignment.getIndexInShift()).isEqualTo(DEFAULT_INDEX_IN_SHIFT);
        assertThat(testShiftAssignment.isIsNeedToReassign()).isEqualTo(DEFAULT_IS_NEED_TO_REASSIGN);
        assertThat(testShiftAssignment.isIsDropped()).isEqualTo(DEFAULT_IS_DROPPED);
        assertThat(testShiftAssignment.isLocked()).isEqualTo(DEFAULT_LOCKED);
    }

    @Test
    public void getAllShiftAssignments() throws Exception {
        // Initialize the database
        shiftAssignmentRepository.saveAndFlush(shiftAssignment);

        // Get all the shiftAssignments
        restShiftAssignmentMockMvc.perform(get("/api/shift-assignments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(shiftAssignment.getId().intValue())))
                .andExpect(jsonPath("$.[*].indexInShift").value(hasItem(DEFAULT_INDEX_IN_SHIFT)))
                .andExpect(jsonPath("$.[*].isNeedToReassign").value(hasItem(DEFAULT_IS_NEED_TO_REASSIGN.booleanValue())))
                .andExpect(jsonPath("$.[*].isDropped").value(hasItem(DEFAULT_IS_DROPPED.booleanValue())))
                .andExpect(jsonPath("$.[*].locked").value(hasItem(DEFAULT_LOCKED.booleanValue())));
    }

    @Test
    public void getShiftAssignment() throws Exception {
        // Initialize the database
        shiftAssignmentRepository.saveAndFlush(shiftAssignment);

        // Get the shiftAssignment
        restShiftAssignmentMockMvc.perform(get("/api/shift-assignments/{id}", shiftAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(shiftAssignment.getId().intValue()))
            .andExpect(jsonPath("$.indexInShift").value(DEFAULT_INDEX_IN_SHIFT))
            .andExpect(jsonPath("$.isNeedToReassign").value(DEFAULT_IS_NEED_TO_REASSIGN.booleanValue()))
            .andExpect(jsonPath("$.isDropped").value(DEFAULT_IS_DROPPED.booleanValue()))
            .andExpect(jsonPath("$.locked").value(DEFAULT_LOCKED.booleanValue()));
    }

    @Test
    public void getNonExistingShiftAssignment() throws Exception {
        // Get the shiftAssignment
        restShiftAssignmentMockMvc.perform(get("/api/shift-assignments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateShiftAssignment() throws Exception {
        // Initialize the database
        shiftAssignmentRepository.saveAndFlush(shiftAssignment);
        int databaseSizeBeforeUpdate = shiftAssignmentRepository.findAll().size();

        // Update the shiftAssignment
        ShiftAssignment updatedShiftAssignment = new ShiftAssignment();
        updatedShiftAssignment.setId(shiftAssignment.getId());
        updatedShiftAssignment.setIndexInShift(UPDATED_INDEX_IN_SHIFT);
        updatedShiftAssignment.setIsNeedToReassign(UPDATED_IS_NEED_TO_REASSIGN);
        updatedShiftAssignment.setIsDropped(UPDATED_IS_DROPPED);
        updatedShiftAssignment.setLocked(UPDATED_LOCKED);

        restShiftAssignmentMockMvc.perform(put("/api/shift-assignments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedShiftAssignment)))
                .andExpect(status().isOk());

        // Validate the ShiftAssignment in the database
        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findAll();
        assertThat(shiftAssignments).hasSize(databaseSizeBeforeUpdate);
        ShiftAssignment testShiftAssignment = shiftAssignments.get(shiftAssignments.size() - 1);
        assertThat(testShiftAssignment.getIndexInShift()).isEqualTo(UPDATED_INDEX_IN_SHIFT);
        assertThat(testShiftAssignment.isIsNeedToReassign()).isEqualTo(UPDATED_IS_NEED_TO_REASSIGN);
        assertThat(testShiftAssignment.isIsDropped()).isEqualTo(UPDATED_IS_DROPPED);
        assertThat(testShiftAssignment.isLocked()).isEqualTo(UPDATED_LOCKED);
    }

    @Test
    public void deleteShiftAssignment() throws Exception {
        // Initialize the database
        shiftAssignmentRepository.saveAndFlush(shiftAssignment);
        int databaseSizeBeforeDelete = shiftAssignmentRepository.findAll().size();

        // Get the shiftAssignment
        restShiftAssignmentMockMvc.perform(delete("/api/shift-assignments/{id}", shiftAssignment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findAll();
        assertThat(shiftAssignments).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getShiftAssignmentsReturnsSortedByDayIndexList() throws Exception {
        ShiftAssignment shiftAssignment1 = createShiftAssignment(createShift(createShiftDate(3), shiftType_E), 1);
        ShiftAssignment shiftAssignment2 = createShiftAssignment(createShift(createShiftDate(1), shiftType_E), 1);
        ShiftAssignment shiftAssignment3 = createShiftAssignment(createShift(createShiftDate(2), shiftType_E), 1);

        restShiftAssignmentMockMvc.perform(get("/api/shift-assignments"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(contains(shiftAssignment2.getId().intValue(), shiftAssignment3.getId().intValue(), shiftAssignment1.getId().intValue())))
            .andExpect(jsonPath("$.[*].shift.shiftDate.dayIndex").value(contains(1, 2, 3)));
    }

    @Test
    public void getShiftAssignmentForShiftDate() throws Exception {
        ShiftDate shiftDate = createShiftDate(3);
        Shift shift = createShift(shiftDate, shiftType_E);
        ShiftAssignment shiftAssignment1 = createShiftAssignment(shift, 2);
        ShiftAssignment shiftAssignment2 = createShiftAssignment(shift, 1);
        ShiftAssignment shiftAssignment3 = createShiftAssignment(createShift(shiftDate, shiftType_L), 1);
        ShiftAssignment shiftAssignment4 = createShiftAssignment(createShift(createShiftDate(2), shiftType_E), 1);

        restShiftAssignmentMockMvc.perform(get("/api/shift-assignments").param("fromShiftDate", shiftDate.getId().toString()).param("toShiftDate", shiftDate.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(contains(
                shiftAssignment2.getId().intValue(),
                shiftAssignment1.getId().intValue(),
                shiftAssignment3.getId().intValue()
            )));
    }
}
