package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.Employee;
import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.repository.EmployeeRepository;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;
import com.teammachine.staffrostering.repository.ShiftDateRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restShiftAssignmentMockMvc;


    private ShiftDate shiftDate_25, shiftDate_26, shiftDate_27;

    private Employee employee_001, employee_002, employee_003;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScheduleResource scheduleResource = new ScheduleResource();
        ReflectionTestUtils.setField(scheduleResource, "employeeRepository", employeeRepository);
        ReflectionTestUtils.setField(scheduleResource, "shiftAssignmentRepository", shiftAssignmentRepository);
        ReflectionTestUtils.setField(scheduleResource, "shiftDateRepository", shiftDateRepository);
        this.restShiftAssignmentMockMvc = MockMvcBuilders.standaloneSetup(scheduleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
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

    @Test
    public void getScheduleNoSuchEmployee() throws Exception {
        restShiftAssignmentMockMvc.perform(get("/api/schedules")
            .param("employee", "10039")
            .param("from", shiftDate_25.getId().toString())
            .param("to", shiftDate_27.getId().toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getScheduleNoSuchShiftDateFromParam() throws Exception {
        restShiftAssignmentMockMvc.perform(get("/api/schedules")
            .param("employee", employee_001.getId().toString())
            .param("from", "101030123")
            .param("to", shiftDate_27.getId().toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getScheduleNoSuchShiftDateToParam() throws Exception {
        restShiftAssignmentMockMvc.perform(get("/api/schedules")
            .param("employee", employee_001.getId().toString())
            .param("from", shiftDate_26.getId().toString())
            .param("to", "101030123"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getScheduleNoShiftAssignments() throws Exception {
        restShiftAssignmentMockMvc.perform(get("/api/schedules")
            .param("employee", employee_001.getId().toString())
            .param("from", shiftDate_27.getId().toString())
            .param("to", shiftDate_27.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*]").isEmpty());
    }
}
