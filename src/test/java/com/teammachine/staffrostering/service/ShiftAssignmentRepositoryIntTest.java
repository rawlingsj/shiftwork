package com.teammachine.staffrostering.service;

import com.google.common.collect.ImmutableSet;
import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.*;
import com.teammachine.staffrostering.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class ShiftAssignmentRepositoryIntTest {

    private static final LocalDate DATE_2016_07_25 = LocalDate.of(2016, Month.JULY, 25);
    private static final LocalDate DATE_2016_07_26 = LocalDate.of(2016, Month.JULY, 26);
    private static final LocalDate DATE_2016_07_27 = LocalDate.of(2016, Month.JULY, 27);

    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;
    @Inject
    private ShiftRepository shiftRepository;
    @Inject
    private ShiftTypeRepository shiftTypeRepository;
    @Inject
    private ShiftDateRepository shiftDateRepository;
    @Inject
    private EmployeeRepository employeeRepository;
    @Inject
    private TaskRepository taskRepository;

    private Task task_1, task_2;

    private ShiftType shiftType_E, shiftType_L, shiftType_N;

    private ShiftDate shiftDate_25, shiftDate_26, shiftDate_27;

    private Employee employee_001, employee_002, employee_003;

    private Shift shift_E25, shift_L25, shift_N25, shift_E26, shift_L26, shift_N26, shift_E27, shift_L27, shift_N27;

    @Before
    public void before() {
        // create shift types
        shiftType_E = createShiftType("E", 1);
        shiftType_L = createShiftType("L", 2);
        shiftType_N = createShiftType("N", 3);

        // create tasks
        task_1 = createTask("Task_1");
        task_2 = createTask("Task_2");

        // create shift dates
        shiftDate_25 = createShiftDate(1, DATE_2016_07_25);
        shiftDate_26 = createShiftDate(2, DATE_2016_07_26);
        shiftDate_27 = createShiftDate(3, DATE_2016_07_27);

        // create employees
        employee_001 = createEmployee("001");
        employee_002 = createEmployee("002");
        employee_003 = createEmployee("003");

        // create shifts
        shift_E25 = createShift(shiftDate_25, shiftType_E);
        shift_L25 = createShift(shiftDate_25, shiftType_L);
        shift_N25 = createShift(shiftDate_25, shiftType_N);
        shift_E26 = createShift(shiftDate_26, shiftType_E);
        shift_L26 = createShift(shiftDate_26, shiftType_L);
        shift_N26 = createShift(shiftDate_26, shiftType_N);
        shift_E27 = createShift(shiftDate_27, shiftType_E);
        shift_L27 = createShift(shiftDate_27, shiftType_L);
        shift_N27 = createShift(shiftDate_27, shiftType_N);
    }

    @Test
    public void findShiftAssignmentForShiftDate() {
        // create shift assignments
        ShiftAssignment shiftAssignment_E25 = createShiftAssignment(shift_E25, employee_001, task_1, task_2);
        ShiftAssignment shiftAssignment_L25 = createShiftAssignment(shift_L25, employee_002, task_1, task_2);
        ShiftAssignment shiftAssignment_N25 = createShiftAssignment(shift_N25, employee_003, task_1, task_2);
        ShiftAssignment shiftAssignment_E26 = createShiftAssignment(shift_E26, employee_001, task_1, task_2);
        ShiftAssignment shiftAssignment_L26 = createShiftAssignment(shift_L26, employee_002, task_1, task_2);
        ShiftAssignment shiftAssignment_N26 = createShiftAssignment(shift_N26, employee_003, task_1, task_2);
        ShiftAssignment shiftAssignment_E27 = createShiftAssignment(shift_E27, employee_001, task_1, task_2);
        ShiftAssignment shiftAssignment_L27 = createShiftAssignment(shift_L27, employee_002, task_1, task_2);
        ShiftAssignment shiftAssignment_N27 = createShiftAssignment(shift_N27, employee_003, task_1, task_2);

        // Business method
        List<ShiftAssignment> shiftAssignments_26 = shiftAssignmentRepository.findAllBetweenShiftDates(shiftDate_26.getDayIndex(), shiftDate_26.getDayIndex()).stream()
            .sorted(Comparator.comparing(sa -> sa.getShift().getShiftType().getIndex())).collect(Collectors.toList());

        // Asserts
        assertThat(shiftAssignments_26).hasSize(3);
        assertThat(shiftAssignments_26).containsOnly(shiftAssignment_E26, shiftAssignment_L26, shiftAssignment_N26);

        ShiftAssignment shiftAssignment;
        shiftAssignment = shiftAssignments_26.get(0);
        assertThat(shiftAssignment.getShift().getShiftType().getIndex()).isEqualTo(1);
        assertThat(shiftAssignment.getShift()).isEqualTo(shift_E26);
        assertThat(shiftAssignment.getShift().getShiftDate()).isEqualTo(shiftDate_26);
        assertThat(shiftAssignment.getEmployee()).isEqualTo(employee_001);
        assertThat(shiftAssignment.getTaskList()).contains(task_1, task_2);

        shiftAssignment = shiftAssignments_26.get(1);
        assertThat(shiftAssignment.getShift().getShiftType().getIndex()).isEqualTo(2);
        assertThat(shiftAssignment.getShift()).isEqualTo(shift_L26);
        assertThat(shiftAssignment.getShift().getShiftDate()).isEqualTo(shiftDate_26);
        assertThat(shiftAssignment.getEmployee()).isEqualTo(employee_002);
        assertThat(shiftAssignment.getTaskList()).contains(task_1, task_2);

        shiftAssignment = shiftAssignments_26.get(2);
        assertThat(shiftAssignment.getShift().getShiftType().getIndex()).isEqualTo(3);
        assertThat(shiftAssignment.getShift()).isEqualTo(shift_N26);
        assertThat(shiftAssignment.getShift().getShiftDate()).isEqualTo(shiftDate_26);
        assertThat(shiftAssignment.getEmployee()).isEqualTo(employee_003);
        assertThat(shiftAssignment.getTaskList()).contains(task_1, task_2);
    }

    @Test
    public void findByEmployee() {
        ShiftAssignment shiftAssignment_25 = createShiftAssignment(shift_E25, employee_001);
        createShiftAssignment(shift_E25, employee_002);
        createShiftAssignment(shift_E25, employee_003);

        ShiftAssignment shiftAssignment_26 = createShiftAssignment(shift_E26, employee_001);
        createShiftAssignment(shift_E26, employee_002);

        ShiftAssignment shiftAssignment_27 = createShiftAssignment(shift_E27, employee_001);

        // Business method
        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findForEmployee(employee_001, DATE_2016_07_25, DATE_2016_07_27);
        // Asserts
        assertThat(shiftAssignments).hasSize(3);
        assertThat(shiftAssignments).containsOnly(shiftAssignment_25, shiftAssignment_26, shiftAssignment_27);

        // Business method
        shiftAssignments = shiftAssignmentRepository.findForEmployee(employee_001, DATE_2016_07_26, DATE_2016_07_26);
        // Asserts
        assertThat(shiftAssignments).hasSize(1);
        assertThat(shiftAssignments).containsOnly(shiftAssignment_26);

        // Business method
        shiftAssignments = shiftAssignmentRepository.findForEmployee(employee_003, DATE_2016_07_26, DATE_2016_07_26);
        // Asserts
        assertThat(shiftAssignments).isEmpty();
    }

    @Test
    public void findAllForShiftDatesAndShiftTypes() {
        ShiftAssignment shiftAssignment_E25_001 = createShiftAssignment(shift_E25, employee_001, task_1);
        ShiftAssignment shiftAssignment_E25_002 = createShiftAssignment(shift_E25, employee_002, task_1);
        ShiftAssignment shiftAssignment_L25_003 = createShiftAssignment(shift_L25, employee_003, task_1);
        ShiftAssignment shiftAssignment_E26_002 = createShiftAssignment(shift_E26, employee_002, task_2);
        ShiftAssignment shiftAssignment_L26_001 = createShiftAssignment(shift_L26, employee_001, task_2);
        ShiftAssignment shiftAssignment_E27_002 = createShiftAssignment(shift_E27, employee_002, task_1, task_2);
        ShiftAssignment shiftAssignment_E27_003 = createShiftAssignment(shift_E27, employee_003, task_1, task_2);

        // Business method
        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findAllForShifts(ImmutableSet.of(shift_E25, shift_L26));
        // Asserts
        assertThat(shiftAssignments).containsOnly(shiftAssignment_E25_001, shiftAssignment_E25_002, shiftAssignment_L26_001);

        // Business method
        shiftAssignments = shiftAssignmentRepository.findAllForShifts(ImmutableSet.of(shift_N25));
        // Asserts
        assertThat(shiftAssignments).isEmpty();
    }

    private ShiftDate createShiftDate(int index, LocalDate date) {
        ShiftDate shiftDate = new ShiftDate();
        shiftDate.setDayIndex(index);
        shiftDate.setDate(date);
        shiftDateRepository.save(shiftDate);
        return shiftDate;
    }

    private ShiftType createShiftType(String code, int index) {
        ShiftType shiftType = new ShiftType();
        shiftType.setCode(code);
        shiftType.setIndex(index);
        shiftTypeRepository.save(shiftType);
        return shiftType;
    }

    private Shift createShift(ShiftDate shiftDate, ShiftType shiftType) {
        Shift shift = new Shift();
        shift.setShiftDate(shiftDate);
        shift.setShiftType(shiftType);
        shiftRepository.save(shift);
        return shift;
    }

    private Employee createEmployee(String code) {
        Employee employee = new Employee();
        employee.setCode(code);
        employeeRepository.save(employee);
        return employee;
    }

    private Task createTask(String code) {
        Task task = new Task();
        task.setCode(code);
        taskRepository.save(task);
        return task;
    }

    private ShiftAssignment createShiftAssignment(Shift shift, Employee employee, Task... tasks) {
        ShiftAssignment shiftAssignment = new ShiftAssignment();
        shiftAssignment.setShift(shift);
        shiftAssignment.setEmployee(employee);
        shiftAssignment.setTaskList(ImmutableSet.copyOf(tasks));
        shiftAssignmentRepository.save(shiftAssignment);
        return shiftAssignment;
    }
}
