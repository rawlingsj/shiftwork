package com.teammachine.staffrostering.web.rest;

import com.google.common.collect.ImmutableSet;
import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.Shift;
import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.domain.ShiftType;
import com.teammachine.staffrostering.domain.Task;
import com.teammachine.staffrostering.domain.enumeration.TaskImportance;
import com.teammachine.staffrostering.domain.enumeration.TaskType;
import com.teammachine.staffrostering.domain.enumeration.TaskUrgency;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;
import com.teammachine.staffrostering.repository.ShiftRepository;
import com.teammachine.staffrostering.repository.ShiftTypeRepository;
import com.teammachine.staffrostering.repository.TaskRepository;
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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private static final Integer DEFAULT_STAFF_REQUIRED = 2;
    private static final Integer UPDATED_STAFF_REQUIRED = 3;

    @Inject
    private ShiftRepository shiftRepository;

    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;

    @Inject
    private ShiftTypeRepository shiftTypeRepository;

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restShiftMockMvc;

    private Shift shift;

    private ShiftType shiftType;

    private Set<Task> shiftTypeTasks;

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
        shiftType = createShiftType("E");
        shiftTypeTasks = shiftType.getTasks();
        shift = new Shift();
        shift.setIndex(DEFAULT_INDEX);
        shift.setStaffRequired(DEFAULT_STAFF_REQUIRED);
        shift.setShiftType(shiftType);
    }

    private ShiftType createShiftType(String code) {
        ShiftType shiftType = new ShiftType();
        shiftType.setCode(code);
        shiftType.setStartTime("06:30");
        shiftType.setEndTime("14:30");
        shiftType.setTasks(ImmutableSet.of(createTask("a"), createTask("b")));
        shiftTypeRepository.saveAndFlush(shiftType);
        return shiftType;
    }

    private Task createTask(String code) {
        Task task = new Task();
        task.setCode(code);
        task.setTaskType(TaskType.FULL);
        task.setImportance(TaskImportance.IMPORTANT);
        task.setUrgency(TaskUrgency.URGENT);
        taskRepository.saveAndFlush(task);
        return task;
    }

    @Test
    public void createShift() throws Exception {
        int databaseSizeBeforeCreate = shiftRepository.findAll().size();
        int shiftAssignmentsSizeBeforeCreate = shiftAssignmentRepository.findAll().size();

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
        assertThat(testShift.getShiftType()).isEqualTo(shiftType);

        //Validate the ShiftAssignments in the database
        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findAllWithEagerRelationships().stream().sorted(Comparator.comparing(ShiftAssignment::getIndexInShift)).collect(Collectors.toList());
        assertThat(shiftAssignments).hasSize(shiftAssignmentsSizeBeforeCreate + DEFAULT_STAFF_REQUIRED);
        ShiftAssignment testShiftAssignment;
        //#1
        testShiftAssignment = shiftAssignments.get(shiftAssignments.size() - DEFAULT_STAFF_REQUIRED);
        assertThat(testShiftAssignment.getIndexInShift()).isEqualTo(1);
        assertThat(testShiftAssignment.getShift()).isEqualTo(testShift);
        assertThat(testShiftAssignment.getTaskList()).containsAll(shiftTypeTasks);
        //#2
        testShiftAssignment = shiftAssignments.get(shiftAssignments.size() - DEFAULT_STAFF_REQUIRED + 1);
        assertThat(testShiftAssignment.getIndexInShift()).isEqualTo(2);
        assertThat(testShiftAssignment.getShift()).isEqualTo(testShift);
        assertThat(testShiftAssignment.getTaskList()).containsAll(shiftTypeTasks);
    }

    @Test
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
    public void getNonExistingShift() throws Exception {
        // Get the shift
        restShiftMockMvc.perform(get("/api/shifts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
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
