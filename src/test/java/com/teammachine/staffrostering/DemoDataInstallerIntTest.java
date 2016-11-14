package com.teammachine.staffrostering;

import com.teammachine.staffrostering.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class DemoDataInstallerIntTest {

    @Inject
    private WeekendDefinitionRepository weekendDefinitionRepository;
    @Inject
    private ContractRepository contractRepository;
    @Inject
    private ContractLineRepository contractLineRepository;
    @Inject
    private SkillRepository skillRepository;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private TaskSkillRequirementRepository taskSkillRequirementRepository;
    @Inject
    private SkillProficiencyRepository skillProficiencyRepository;
    @Inject
    private ShiftTypeRepository shiftTypeRepository;
    @Inject
    private ShiftDateRepository shiftDateRepository;
    @Inject
    private ShiftRepository shiftRepository;
    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;
    @Inject
    private StaffRosterParametrizationRepository staffRosterParametrizationRepository;
    @Inject
    private EmployeeRepository employeeRepository;
    @Inject
    private EmployeeAbsentReasonRepository employeeAbsentReasonRepository;
    @Inject
    private EmployeeLeaveAbsenceRepository employeeLeaveAbsenceRepository;

    @Test
    @Transactional
    public void install() {
        DemoDataInstaller demoDataInstaller = new DemoDataInstaller();
        ReflectionTestUtils.setField(demoDataInstaller, "weekendDefinitionRepository", weekendDefinitionRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "contractRepository", contractRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "contractLineRepository", contractLineRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "skillRepository", skillRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "taskRepository", taskRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "taskSkillRequirementRepository", taskSkillRequirementRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "skillProficiencyRepository", skillProficiencyRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "shiftTypeRepository", shiftTypeRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "shiftDateRepository", shiftDateRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "shiftRepository", shiftRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "shiftAssignmentRepository", shiftAssignmentRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "staffRosterParametrizationRepository", staffRosterParametrizationRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "employeeRepository", employeeRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "employeeAbsentReasonRepository", employeeAbsentReasonRepository);
        ReflectionTestUtils.setField(demoDataInstaller, "employeeLeaveAbsenceRepository", employeeLeaveAbsenceRepository);

        // Business method
        demoDataInstaller.install();

        // Asserts
        // at least should not fail
    }
}
