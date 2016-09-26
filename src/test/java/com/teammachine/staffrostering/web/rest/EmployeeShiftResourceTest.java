package com.teammachine.staffrostering.web.rest;

import com.google.common.collect.ImmutableSet;
import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.*;
import com.teammachine.staffrostering.repository.*;
import com.teammachine.staffrostering.web.rest.errors.ErrorConstants;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class EmployeeShiftResourceTest {

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
        EmployeeShiftResource employeeScheduleResource = new EmployeeShiftResource();
        ReflectionTestUtils.setField(employeeScheduleResource, "employeeRepository", employeeRepository);
        ReflectionTestUtils.setField(employeeScheduleResource, "shiftAssignmentRepository", shiftAssignmentRepository);
        this.restShiftAssignmentMockMvc = MockMvcBuilders.standaloneSetup(employeeScheduleResource)
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
    public void getShiftsNoSuchEmployee() throws Exception {
        restShiftAssignmentMockMvc.perform(get("/api/employeeshifts")
            .param("employee", "10039")
            .param("fromDate", asRequestParam(DATE_2016_07_25))
            .param("toDate", asRequestParam(DATE_2016_07_27)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorConstants.ERR_NO_SUCH_EMPLOYEE))
            .andExpect(jsonPath("$.params[*]").value(contains("10039")));
    }

    @Test
    public void getShiftsNoShiftAssignments() throws Exception {
        restShiftAssignmentMockMvc.perform(get("/api/employeeshifts")
            .param("employee", employee_001.getId().toString())
            .param("fromDate", asRequestParam(DATE_2016_07_27))
            .param("toDate", asRequestParam(DATE_2016_07_27)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*]").isEmpty());
    }

    @Test
    public void getShifts() throws Exception {
        ShiftType shiftType = createShiftType("E", 1);
        Task task_1 = createTask("1");
        Task task_2 = createTask("2");
        Shift shift_25 = createShift(shiftDate_25, shiftType);
        Shift shift_26 = createShift(shiftDate_26, shiftType);
        Shift shift_27 = createShift(shiftDate_27, shiftType);
        createShiftAssignment(shift_25, employee_001, task_1, task_2);
        createShiftAssignment(shift_25, employee_002, task_1);
        createShiftAssignment(shift_26, employee_001, task_1);
        createShiftAssignment(shift_26, employee_003, task_2);
        createShiftAssignment(shift_27, employee_002, task_1, task_2);
        createShiftAssignment(shift_27, employee_003, task_1);
        createShiftAssignment(shift_27, employee_001, task_1);

        restShiftAssignmentMockMvc.perform(get("/api/employeeshifts")
            .param("employee", employee_001.getId().toString())
            .param("fromDate", asRequestParam(DATE_2016_07_25))
            .param("toDate", asRequestParam(DATE_2016_07_26)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].shiftType.id").value(contains(shiftType.getId().intValue(), shiftType.getId().intValue())))
            .andExpect(jsonPath("$.[*].shiftDate.id").value(contains(shiftDate_25.getId().intValue(), shiftDate_26.getId().intValue())))
            .andExpect(jsonPath("$.[0].tasks[*].id").value(contains(task_1.getId().intValue(), task_2.getId().intValue())))
            .andExpect(jsonPath("$.[1].tasks[*].id").value(contains(task_1.getId().intValue())))
            .andExpect(jsonPath("$.[0].coworkers[*].id").value(contains(employee_002.getId().intValue())))
            .andExpect(jsonPath("$.[1].coworkers[*]").isEmpty());
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
        shiftAssignmentRepository.save(shiftAssignment);
        return shiftAssignment;
    }
}
