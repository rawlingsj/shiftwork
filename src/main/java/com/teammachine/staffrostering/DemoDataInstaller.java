package com.teammachine.staffrostering;

import com.teammachine.staffrostering.config.Constants;
import com.teammachine.staffrostering.domain.*;
import com.teammachine.staffrostering.domain.enumeration.*;
import com.teammachine.staffrostering.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Installs demo/test data - entities that should be then used for plan generation.<p>
 * Enabled only if environment variable "createDemoData" is specified at start time.
 */
@Component
@Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
@Conditional(DemoDataInstaller.CreateDemoDataCondition.class)
class DemoDataInstaller {

    private final Logger logger = LoggerFactory.getLogger(DemoDataInstaller.class);

    static class CreateDemoDataCondition implements Condition {

        public static final String CREATE_DEMO_DATA_PROPERTY = "createDemoData";

        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            Environment environment = conditionContext.getEnvironment();
            return environment != null && environment.getProperty(CREATE_DEMO_DATA_PROPERTY) != null;
        }
    }

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

    @PostConstruct
    private void install() {
        logger.info("----- Installation of demo data -----");

        // Weekend definition
        WeekendDefinition saturdayAndSunday = createWeekendDefinition("Saturday and Sunday", DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        logger.info("* weekend definition");

        // Full time contract
        Contract fullTimeContract = createContract("0", "fulltime", saturdayAndSunday);
        addBooleanContractLine(fullTimeContract, ContractLineType.SINGLE_ASSIGNMENT_PER_DAY, 1);
        addBooleanContractLine(fullTimeContract, ContractLineType.COMPLETE_WEEKENDS, 1);
        addBooleanContractLine(fullTimeContract, ContractLineType.IDENTICAL_SHIFT_TYPES_DURING_WEEKEND, 1);
        addMinMaxContractLine(fullTimeContract, ContractLineType.TOTAL_ASSIGNMENTS, 9, 1, 16, 1);
        addMinMaxContractLine(fullTimeContract, ContractLineType.CONSECUTIVE_WORKING_DAYS, 2, 1, 8, 1);
        addMinMaxContractLine(fullTimeContract, ContractLineType.CONSECUTIVE_FREE_DAYS, 1, 1, 7, 1);
        // 75% contract
        Contract time75Contract = createContract("1", "75_time", saturdayAndSunday);
        addBooleanContractLine(time75Contract, ContractLineType.SINGLE_ASSIGNMENT_PER_DAY, 1);
        addBooleanContractLine(time75Contract, ContractLineType.COMPLETE_WEEKENDS, 1);
        addBooleanContractLine(time75Contract, ContractLineType.IDENTICAL_SHIFT_TYPES_DURING_WEEKEND, 1);
        addMinMaxContractLine(time75Contract, ContractLineType.TOTAL_ASSIGNMENTS, 6, 1, 12, 1);
        addMinMaxContractLine(time75Contract, ContractLineType.CONSECUTIVE_WORKING_DAYS, 2, 1, 5, 1);
        addMinMaxContractLine(time75Contract, ContractLineType.CONSECUTIVE_FREE_DAYS, 1, 1, 5, 1);
        // 50% contract
        Contract time50Contract = createContract("2", "50_time", saturdayAndSunday);
        addBooleanContractLine(time50Contract, ContractLineType.SINGLE_ASSIGNMENT_PER_DAY, 1);
        addBooleanContractLine(time50Contract, ContractLineType.COMPLETE_WEEKENDS, 1);
        addBooleanContractLine(time50Contract, ContractLineType.IDENTICAL_SHIFT_TYPES_DURING_WEEKEND, 1);
        addMinMaxContractLine(time50Contract, ContractLineType.TOTAL_ASSIGNMENTS, 4, 1, 9, 1);
        addMinMaxContractLine(time50Contract, ContractLineType.CONSECUTIVE_WORKING_DAYS, 2, 1, 4, 1);
        addMinMaxContractLine(time50Contract, ContractLineType.CONSECUTIVE_FREE_DAYS, 1, 1, 7, 1);
        // night contract
        Contract nightContract = createContract("3", "night", saturdayAndSunday);
        addBooleanContractLine(nightContract, ContractLineType.SINGLE_ASSIGNMENT_PER_DAY, 1);
        addBooleanContractLine(nightContract, ContractLineType.COMPLETE_WEEKENDS, 1);
        addBooleanContractLine(nightContract, ContractLineType.IDENTICAL_SHIFT_TYPES_DURING_WEEKEND, 1);
        addMinMaxContractLine(nightContract, ContractLineType.TOTAL_ASSIGNMENTS, 8, 1, 8, 1);
        addMinMaxContractLine(nightContract, ContractLineType.CONSECUTIVE_WORKING_DAYS, 1, 1, 2, 1);
        addMinMaxContractLine(nightContract, ContractLineType.CONSECUTIVE_FREE_DAYS, 3, 1, 20, 1);

        logger.info("* contracts with contract lines");

        // tasks
        Task task1 = createTask("T1", "Task1", 1, TaskType.SHORT, TaskImportance.NOT_IMPORTANT, TaskUrgency.URGENT);
        Task task2 = createTask("T2", "Task2", 1, TaskType.MAIN, TaskImportance.IMPORTANT, TaskUrgency.URGENT);
        Task task3 = createTask("T3", "Task3", 1, TaskType.SHORT, TaskImportance.NOT_IMPORTANT, TaskUrgency.URGENT);
        Task task4 = createTask("T4", "Task4", 1, TaskType.MAIN, TaskImportance.IMPORTANT, TaskUrgency.NOT_URGENT);
        Task task5 = createTask("T5", "Task5", 1, TaskType.FULL, TaskImportance.IMPORTANT, TaskUrgency.NOT_URGENT);
        Task task6 = createTask("T6", "Task6", 1, TaskType.FULL, TaskImportance.IMPORTANT, TaskUrgency.NOT_URGENT);
        logger.info("* tasks");

        // skills
        Skill skill1 = createSkill("Skill1");
        Skill skill2 = createSkill("Skill2");
        Skill skill3 = createSkill("Skill3");
        logger.info("* skills");

        // task skill requirements
        createTaskSkillRequirement(task1, skill1);
        createTaskSkillRequirement(task2, skill2);
        createTaskSkillRequirement(task3, skill3);
        createTaskSkillRequirement(task4, skill1);
        createTaskSkillRequirement(task5, skill2);
        createTaskSkillRequirement(task6, skill3);
        logger.info("* task skill requirements");

        // shift type
        ShiftType shiftTypeE = createShiftType("E", "Early", 0, LocalTime.of(6, 30), LocalTime.of(14, 30), false, task1, task2);
        ShiftType shiftTypeL = createShiftType("L", "Late", 1, LocalTime.of(14, 30), LocalTime.of(22, 30), false, task2, task3);
        ShiftType shiftTypeD = createShiftType("D", "Day shift", 2, LocalTime.of(8, 30), LocalTime.of(16, 30), false, task3, task4);
        ShiftType shiftTypeN = createShiftType("N", "Night", 3, LocalTime.of(22, 30), LocalTime.of(6, 30), true, task5, task6);
        logger.info("* shift types");

        // shiftDates
        LocalDate firstDate = LocalDate.of(2010, 1, 1);
        LocalDate lastDate = LocalDate.of(2010, 1, 28);
        LocalDate localDate = firstDate;
        ShiftDate firstShiftDate = null, lastShiftDate = null;
        while (!localDate.isAfter(lastDate)) {
            ShiftDate shiftDate = createShiftDate(localDate);
            if (localDate.equals(firstDate)) {
                firstShiftDate = shiftDate;
            }
            if (localDate.equals(lastDate)) {
                lastShiftDate = shiftDate;
            }
            createShiftWithAssignments(0, shiftDate, shiftTypeE, 2);
            createShiftWithAssignments(1, shiftDate, shiftTypeL, 2);
            createShiftWithAssignments(2, shiftDate, shiftTypeD, 2);
            createShiftWithAssignments(3, shiftDate, shiftTypeN, 1);
            localDate = localDate.plus(1, ChronoUnit.DAYS);
        }
        logger.info("* shifts");

        // staff roster parameterization
        createParameterization("Parameterization", firstShiftDate, lastShiftDate);
        logger.info("* staff roster parameterization");

        // employees
        Employee a = createEmployee("A", "Employee A", fullTimeContract);
        Employee b = createEmployee("B", "Employee B", fullTimeContract);
        Employee c = createEmployee("C", "Employee C", fullTimeContract);
        Employee d = createEmployee("D", "Employee D", fullTimeContract);
        Employee e = createEmployee("E", "Employee E", time75Contract);
        Employee f = createEmployee("F", "Employee F", time75Contract);
        Employee g = createEmployee("G", "Employee G", time50Contract);
        Employee h = createEmployee("H", "Employee H", time50Contract);
        Employee i = createEmployee("I", "Employee I", nightContract);
        Employee j = createEmployee("J", "Employee J", nightContract);
        logger.info("* employees");

        // skill proficiency
        createSkillProficiency(a, skill1, skill2);
        createSkillProficiency(b, skill2, skill3);
        createSkillProficiency(c, skill3);
        createSkillProficiency(d, skill1, skill2);
        createSkillProficiency(e, skill2, skill3);
        createSkillProficiency(f, skill3);
        createSkillProficiency(g, skill1, skill2);
        createSkillProficiency(h, skill2, skill3);
        createSkillProficiency(i, skill3);
        createSkillProficiency(j, skill1, skill2);
        logger.info("* skill proficiencies");

        // employee absent reason
        EmployeeAbsentReason dayOffReason = createEmployeeAbsentReason("day-off", "Day off", "Day off", 1, DurationUnit.DAYS);
        EmployeeAbsentReason sickReason = createEmployeeAbsentReason("sick", "Sick", "Sick", 5, DurationUnit.DAYS);
        EmployeeAbsentReason maternityLeaveReason = createEmployeeAbsentReason("maternity_leave", "Maternity Leave", "Maternity Leave", 6, DurationUnit.MONTHS);
        logger.info("* employee absent reasons");

        // employee leave absence
        createEmployeeLeaveAbsence(a, ZonedDateTime.of(2016, 6, 1, 0, 0, 0, 0, ZoneId.systemDefault()), ZonedDateTime.of(2016, 6, 2, 0, 0, 0, 0, ZoneId.systemDefault()), dayOffReason);
        createEmployeeLeaveAbsence(b, ZonedDateTime.of(2016, 6, 1, 0, 0, 0, 0, ZoneId.systemDefault()), ZonedDateTime.of(2016, 6, 5, 0, 0, 0, 0, ZoneId.systemDefault()), sickReason);
        createEmployeeLeaveAbsence(c, ZonedDateTime.of(2016, 6, 1, 0, 0, 0, 0, ZoneId.systemDefault()), ZonedDateTime.of(2016, 12, 2, 0, 0, 0, 0, ZoneId.systemDefault()), maternityLeaveReason);
        logger.info("* employee leave absences");

        logger.info("----- Installed demo data -----");
    }

    private WeekendDefinition createWeekendDefinition(String description, DayOfWeek... days) {
        WeekendDefinition weekendDefinition = new WeekendDefinition();
        weekendDefinition.setDescription(description);
        weekendDefinition.setDays(EnumSet.copyOf(Arrays.asList(days)));
        weekendDefinitionRepository.save(weekendDefinition);
        return weekendDefinition;
    }

    private Contract createContract(String code, String description, WeekendDefinition weekend) {
        Contract contract = new Contract();
        contract.setCode(code);
        contract.setDescription(description);
        contract.setWeekendDefinition(weekend);
        contractRepository.save(contract);
        return contract;
    }

    private void addBooleanContractLine(Contract contract, ContractLineType contractLineType, int weight) {
        BooleanContractLine booleanContractLine = new BooleanContractLine();
        booleanContractLine.setContract(contract);
        booleanContractLine.setContractLineType(contractLineType);
        booleanContractLine.setEnabled(true);
        booleanContractLine.setWeight(weight);
        contractLineRepository.save(booleanContractLine);
    }

    private void addMinMaxContractLine(Contract contract, ContractLineType contractLineType, int minValue, int minWeight, int maxValue, int maxWeight) {
        MinMaxContractLine minMaxContractLine = new MinMaxContractLine();
        minMaxContractLine.setContract(contract);
        minMaxContractLine.setContractLineType(contractLineType);
        minMaxContractLine.setMinimumEnabled(true);
        minMaxContractLine.setMinimumValue(minValue);
        minMaxContractLine.setMinimumWeight(minWeight);
        minMaxContractLine.setMaximumEnabled(true);
        minMaxContractLine.setMaximumValue(maxValue);
        minMaxContractLine.setMaximumWeight(maxWeight);
        contractLineRepository.save(minMaxContractLine);
    }

    private Skill createSkill(String code) {
        Skill skill = new Skill();
        skill.setCode(code);
        skillRepository.save(skill);
        return skill;
    }

    private Task createTask(String code, String description, int staffNeeded, TaskType taskType, TaskImportance importance, TaskUrgency urgency) {
        Task task = new Task();
        task.setCode(code);
        task.setDescription(description);
        task.setStaffNeeded(staffNeeded);
        task.setTaskType(taskType);
        task.setImportance(importance);
        task.setUrgency(urgency);
        taskRepository.save(task);
        return task;
    }

    private TaskSkillRequirement createTaskSkillRequirement(Task task, Skill skill) {
        TaskSkillRequirement taskSkillRequirement = new TaskSkillRequirement();
        taskSkillRequirement.setSkill(skill);
        taskSkillRequirement.setTask(task);
        taskSkillRequirementRepository.save(taskSkillRequirement);
        return taskSkillRequirement;
    }

    private ShiftType createShiftType(String code, String description, int index, LocalTime startTime, LocalTime endTime, Boolean nightShift, Task... tasks) {
        ShiftType shiftType = new ShiftType();
        shiftType.setCode(code);
        shiftType.setDescription(description);
        shiftType.setIndex(index);
        shiftType.setStartTime(startTime);
        shiftType.setEndTime(endTime);
        shiftType.setNightShift(nightShift);
        shiftType.setTasks(Arrays.asList(tasks).stream().collect(Collectors.toSet()));
        shiftTypeRepository.save(shiftType);
        return shiftType;
    }

    private ShiftDate createShiftDate(LocalDate date) {
        ShiftDate shiftDate = new ShiftDate();
        shiftDate.setDate(date);
        shiftDate.setDayIndex(Long.valueOf(date.toEpochDay()).intValue());
        shiftDate.setDayOfWeek(getDayOfWeek(date.getDayOfWeek()));
        shiftDateRepository.save(shiftDate);
        return shiftDate;
    }

    private DayOfWeek getDayOfWeek(java.time.DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return DayOfWeek.MONDAY;
            case TUESDAY:
                return DayOfWeek.TUESDAY;
            case WEDNESDAY:
                return DayOfWeek.WEDNESDAY;
            case THURSDAY:
                return DayOfWeek.THURSDAY;
            case FRIDAY:
                return DayOfWeek.FRIDAY;
            case SATURDAY:
                return DayOfWeek.SATURDAY;
            case SUNDAY:
                return DayOfWeek.SUNDAY;
        }
        return null;
    }

    private Shift createShiftWithAssignments(int index, ShiftDate shiftDate, ShiftType shiftType, int staffRequired) {
        Shift shift = new Shift();
        shift.setIndex(index);
        shift.setShiftDate(shiftDate);
        shift.setShiftType(shiftType);
        shift.setStaffRequired(staffRequired);
        shiftRepository.save(shift);
        List<ShiftAssignment> shiftAssignments = new ArrayList<>(staffRequired);
        for (int i = 0; i < staffRequired; i++) {
            ShiftAssignment shiftAssignment = new ShiftAssignment();
            shiftAssignment.setIndexInShift(i);
            shiftAssignment.setShift(shift);
            shiftAssignment.setTaskList(shift.getShiftType().getTasks().stream().collect(Collectors.toSet()));
            shiftAssignments.add(shiftAssignment);
        }
        shiftAssignmentRepository.save(shiftAssignments);
        return shift;
    }

    private StaffRosterParametrization createParameterization(String name, ShiftDate firstShiftDate, ShiftDate lastShiftDate) {
        StaffRosterParametrization staffRosterParametrization = new StaffRosterParametrization();
        staffRosterParametrization.setName(name);
        staffRosterParametrization.setFirstShiftDate(firstShiftDate);
        staffRosterParametrization.setPlanningWindowStart(firstShiftDate);
        staffRosterParametrization.setLastShiftDate(lastShiftDate);
        staffRosterParametrization.setPlanningWindowEnd(lastShiftDate);
        staffRosterParametrizationRepository.save(staffRosterParametrization);
        return staffRosterParametrization;
    }

    private Employee createEmployee(String code, String name, Contract contract) {
        Employee employee = new Employee();
        employee.setCode(code);
        employee.setName(name);
        employee.setContract(contract);
        employeeRepository.save(employee);
        return employee;
    }

    private SkillProficiency createSkillProficiency(Employee employee, Skill... skills) {
        SkillProficiency skillProficiency = new SkillProficiency();
        skillProficiency.setEmployee(employee);
        skillProficiency.setSkillList(Arrays.asList(skills).stream().collect(Collectors.toSet()));
        skillProficiencyRepository.save(skillProficiency);
        return skillProficiency;
    }

    private EmployeeAbsentReason createEmployeeAbsentReason(String code, String name, String description, Integer duration, DurationUnit durationUnit) {
        EmployeeAbsentReason employeeAbsentReason = new EmployeeAbsentReason();
        employeeAbsentReason.setCode(code);
        employeeAbsentReason.setName(name);
        employeeAbsentReason.setDescription(description);
        employeeAbsentReason.setDefaultDuration(duration);
        employeeAbsentReason.setDefaultDurationUnit(durationUnit);
        employeeAbsentReasonRepository.save(employeeAbsentReason);
        return employeeAbsentReason;
    }

    private EmployeeLeaveAbsence createEmployeeLeaveAbsence(Employee employee, ZonedDateTime from, ZonedDateTime to, EmployeeAbsentReason reason) {
        EmployeeLeaveAbsence employeeLeaveAbsence = new EmployeeLeaveAbsence();
        employeeLeaveAbsence.setEmployee(employee);
        employeeLeaveAbsence.setAbsentFrom(from);
        employeeLeaveAbsence.setAbsentTo(to);
        employeeLeaveAbsence.setReason(reason);
        employeeLeaveAbsenceRepository.save(employeeLeaveAbsence);
        return employeeLeaveAbsence;
    }
}

