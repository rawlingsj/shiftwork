package com.teammachine.staffrostering.web.rest;

import com.google.common.collect.ImmutableSet;
import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.*;
import com.teammachine.staffrostering.repository.*;
import com.teammachine.staffrostering.web.rest.errors.ExceptionTranslator;
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
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class ScheduleResourceIntTest {

    private static final LocalDate DATE_2016_07_25 = LocalDate.of(2016, Month.JULY, 25);
    private static final LocalDate DATE_2016_07_26 = LocalDate.of(2016, Month.JULY, 26);
    private static final LocalDate DATE_2016_07_27 = LocalDate.of(2016, Month.JULY, 27);

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;

    @Inject
    private ShiftDateRepository shiftDateRepository;

    @Inject
    private ShiftTypeRepository shiftTypeRepository;

    @Inject
    private ShiftRepository shiftRepository;

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restShiftAssignmentMockMvc;

    private ShiftDate shiftDate_25, shiftDate_26, shiftDate_27;

    private Employee employee_001, employee_002, employee_003;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScheduleResource scheduleResource = new ScheduleResource();
        ReflectionTestUtils.setField(scheduleResource, "shiftAssignmentRepository", shiftAssignmentRepository);
        ReflectionTestUtils.setField(scheduleResource, "shiftDateRepository", shiftDateRepository);
        ReflectionTestUtils.setField(scheduleResource, "shiftTypeRepository", shiftTypeRepository);
        this.restShiftAssignmentMockMvc = MockMvcBuilders.standaloneSetup(scheduleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter)
            .setControllerAdvice(exceptionTranslator)
            .build();
    }

    @Before
    public void before() {
        // create shiftDates
        shiftDate_25 = createShiftDate(1, DATE_2016_07_25);
        shiftDate_26 = createShiftDate(2, DATE_2016_07_26);
        shiftDate_27 = createShiftDate(3, DATE_2016_07_27);

        // create employees
        employee_001 = createEmployee("001");
        employee_002 = createEmployee("002");
        employee_003 = createEmployee("003");
    }

    private String asRequestParam(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() + "";
    }

    @Test
    public void getDaySchedule() throws Exception {
        ShiftType shiftTypeE = createShiftType("E", 1);
        ShiftType shiftTypeL = createShiftType("L", 2);
        ShiftType shiftTypeN = createShiftType("N", 3);
        Task task_1 = createTask("1");
        Task task_2 = createTask("2");
        Shift shift_25_E = createShift(shiftDate_25, shiftTypeE);
        Shift shift_26_E = createShift(shiftDate_26, shiftTypeE);
        Shift shift_25_L = createShift(shiftDate_25, shiftTypeL);
        Shift shift_25_N = createShift(shiftDate_25, shiftTypeN);
        ShiftAssignment shiftAssignment1 = createShiftAssignment(shift_25_E, employee_001, task_1, task_2);
        ShiftAssignment shiftAssignment2 = createShiftAssignment(shift_25_L, employee_002, task_1);
        ShiftAssignment shiftAssignment3 = createShiftAssignment(shift_25_N, employee_003, task_2);
        createShiftAssignment(shift_26_E, employee_001);

        restShiftAssignmentMockMvc.perform(get("/api/schedules")
            .param("shiftDate", asRequestParam(DATE_2016_07_25)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(contains(
                shiftAssignment1.getId().intValue(),
                shiftAssignment2.getId().intValue(),
                shiftAssignment3.getId().intValue()
            )));
    }

    private ShiftDate createShiftDate(int index, LocalDate date) {
        ShiftDate shiftDate = new ShiftDate();
        shiftDate.setDayIndex(index);
        shiftDate.setDate(date);
        shiftDateRepository.save(shiftDate);
        return shiftDate;
    }

    private Employee createEmployee(String code) {
        Employee employee = new Employee();
        employee.setCode(code);
        employeeRepository.save(employee);
        return employee;
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
        shiftAssignment.setIndexInShift(0);
        shiftAssignmentRepository.save(shiftAssignment);
        return shiftAssignment;
    }
}
