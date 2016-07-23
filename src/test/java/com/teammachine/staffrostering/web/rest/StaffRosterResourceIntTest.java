package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.Employee;
import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.repository.EmployeeRepository;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;
import com.teammachine.staffrostering.repository.StaffRosterParametrizationRepository;
import com.teammachine.staffrostering.web.rest.dto.StaffRosterDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class StaffRosterResourceIntTest {

    @Inject
    private StaffRosterParametrizationRepository staffRosterParametrizationRepository;

    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStaffRosterMockMvc;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StaffRosterResource staffRosterResource = new StaffRosterResource();
        ReflectionTestUtils.setField(staffRosterResource, "staffRosterParametrizationRepository", staffRosterParametrizationRepository);
        ReflectionTestUtils.setField(staffRosterResource, "shiftAssignmentRepository", shiftAssignmentRepository);
        this.restStaffRosterMockMvc = MockMvcBuilders.standaloneSetup(staffRosterResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    public void createStaffRoaster() throws Exception {
        // initial setup
        StaffRosterDTO staffRosterDTO = new StaffRosterDTO();
        StaffRosterParametrization staffRosterParametrization = createStaffRosterParametrization();
        staffRosterDTO.setStaffRosterParametrization(staffRosterParametrization);
        ShiftAssignment shiftAssignment = createShiftAssignment();
        staffRosterDTO.setShiftAssignments(Collections.singletonList(shiftAssignment));

        // update
        String employeeCode = "Employee 001";
        int hardConstraintMatches = 0;
        int softConstraintMatches = -3;
        staffRosterParametrization.setHardConstraintMatches(hardConstraintMatches);
        staffRosterParametrization.setSoftConstraintMatches(softConstraintMatches);
        shiftAssignment.setEmployee(createEmployee(employeeCode));

        // Business method
        restStaffRosterMockMvc.perform(post("/api/staff-rosters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(staffRosterDTO)))
                .andExpect(status().isOk());

        // Asserts
        shiftAssignment = shiftAssignmentRepository.findOneWithEagerRelationships(shiftAssignment.getId());
        assertThat(shiftAssignment.getEmployee()).isNotNull();
        assertThat(shiftAssignment.getEmployee().getCode()).isEqualTo(employeeCode);
        staffRosterParametrization = staffRosterParametrizationRepository.findOne(staffRosterParametrization.getId());
        assertThat(staffRosterParametrization.getHardConstraintMatches()).isEqualTo(hardConstraintMatches);
        assertThat(staffRosterParametrization.getSoftConstraintMatches()).isEqualTo(softConstraintMatches);
    }

    private Employee createEmployee(String code) {
        Employee employee = new Employee();
        employee.setCode(code);
        employeeRepository.saveAndFlush(employee);
        return employee;
    }

    private StaffRosterParametrization createStaffRosterParametrization() {
        StaffRosterParametrization staffRosterParametrization = new StaffRosterParametrization();
        staffRosterParametrization.setName("name");
        staffRosterParametrization.setDescription("description");
        staffRosterParametrization.setHardConstraintMatches(-1);
        staffRosterParametrization.setSoftConstraintMatches(-104);
        staffRosterParametrizationRepository.saveAndFlush(staffRosterParametrization);
        return staffRosterParametrization;
    }

    private ShiftAssignment createShiftAssignment() {
        ShiftAssignment shiftAssignment = new ShiftAssignment();
        shiftAssignmentRepository.saveAndFlush(shiftAssignment);
        return shiftAssignment;
    }
}

