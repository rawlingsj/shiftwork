package com.teammachine.staffrostering.service;

import com.google.common.collect.ImmutableSet;
import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.*;
import com.teammachine.staffrostering.repository.*;
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

    @Test
    public void findShiftAssignmentForShiftDate() {
        // create shift types
        ShiftType shiftType_E = createShiftType("E", 1);
        ShiftType shiftType_L = createShiftType("L", 2);
        ShiftType shiftType_N = createShiftType("N", 3);
        shiftTypeRepository.flush();

        // create shift dates
        ShiftDate shiftDate_25 = createShiftDate(1, DATE_2016_07_25);
        ShiftDate shiftDate_26 = createShiftDate(2, DATE_2016_07_26);
        ShiftDate shiftDate_27 = createShiftDate(3, DATE_2016_07_27);
        shiftDateRepository.flush();

        // create task
        Task task_1 = createTask("Task_1");
        Task task_2 = createTask("Task_2");
        taskRepository.flush();


        // create shifts
        Shift shiftE_25 = createShift(shiftDate_25, shiftType_E);
        Shift shiftL_25 = createShift(shiftDate_25, shiftType_L);
        Shift shiftN_25 = createShift(shiftDate_25, shiftType_N);
        Shift shiftE_26 = createShift(shiftDate_26, shiftType_E);
        Shift shiftL_26 = createShift(shiftDate_26, shiftType_L);
        Shift shiftN_26 = createShift(shiftDate_26, shiftType_N);
        Shift shiftE_27 = createShift(shiftDate_27, shiftType_E);
        Shift shiftL_27 = createShift(shiftDate_27, shiftType_L);
        Shift shiftN_27 = createShift(shiftDate_27, shiftType_N);
        shiftRepository.flush();

        // create employees
        Employee employee001 = createEmployee("001");
        Employee employee002 = createEmployee("002");
        Employee employee003 = createEmployee("003");
        employeeRepository.flush();

        // create shift assignments
        ShiftAssignment shiftAssignmentE_25 = createShiftAssignment(shiftE_25, employee001, task_1, task_2);
        ShiftAssignment shiftAssignmentL_25 = createShiftAssignment(shiftL_25, employee002, task_1, task_2 );
        ShiftAssignment shiftAssignmentN_25 = createShiftAssignment(shiftN_25, employee003, task_1, task_2);
        ShiftAssignment shiftAssignmentE_26 = createShiftAssignment(shiftE_26, employee001, task_1, task_2);
        ShiftAssignment shiftAssignmentL_26 = createShiftAssignment(shiftL_26, employee002, task_1, task_2);
        ShiftAssignment shiftAssignmentN_26 = createShiftAssignment(shiftN_26, employee003, task_1, task_2);
        ShiftAssignment shiftAssignmentE_27 = createShiftAssignment(shiftE_27, employee001, task_1, task_2);
        ShiftAssignment shiftAssignmentL_27 = createShiftAssignment(shiftL_27, employee002, task_1, task_2);
        ShiftAssignment shiftAssignmentN_27 = createShiftAssignment(shiftN_27, employee003, task_1, task_2);
        shiftAssignmentRepository.flush();

        // Business method
        List<ShiftAssignment> shiftAssignments_26 = shiftAssignmentRepository.findAllForShiftDate(shiftDate_26).stream()
            .sorted(Comparator.comparing(sa -> sa.getShift().getShiftType().getIndex())).collect(Collectors.toList());

        // Asserts
        assertThat(shiftAssignments_26).hasSize(3);
        assertThat(shiftAssignments_26).contains(shiftAssignmentE_26, shiftAssignmentL_26, shiftAssignmentN_26);

        ShiftAssignment shiftAssignment;
        shiftAssignment = shiftAssignments_26.get(0);
        assertThat(shiftAssignment.getShift().getShiftType().getIndex()).isEqualTo(1);
        assertThat(shiftAssignment.getShift()).isEqualTo(shiftE_26);
        assertThat(shiftAssignment.getShift().getShiftDate()).isEqualTo(shiftDate_26);
        assertThat(shiftAssignment.getEmployee()).isEqualTo(employee001);
        assertThat(shiftAssignment.getTaskList()).contains(task_1, task_2);

        shiftAssignment = shiftAssignments_26.get(1);
        assertThat(shiftAssignment.getShift().getShiftType().getIndex()).isEqualTo(2);
        assertThat(shiftAssignment.getShift()).isEqualTo(shiftL_26);
        assertThat(shiftAssignment.getShift().getShiftDate()).isEqualTo(shiftDate_26);
        assertThat(shiftAssignment.getEmployee()).isEqualTo(employee002);
        assertThat(shiftAssignment.getTaskList()).contains(task_1, task_2);

        shiftAssignment = shiftAssignments_26.get(2);
        assertThat(shiftAssignment.getShift().getShiftType().getIndex()).isEqualTo(3);
        assertThat(shiftAssignment.getShift()).isEqualTo(shiftN_26);
        assertThat(shiftAssignment.getShift().getShiftDate()).isEqualTo(shiftDate_26);
        assertThat(shiftAssignment.getEmployee()).isEqualTo(employee003);
        assertThat(shiftAssignment.getTaskList()).contains(task_1, task_2);
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
