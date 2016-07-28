package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.Employee;
import com.teammachine.staffrostering.domain.Shift;
import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.repository.EmployeeRepository;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;
import com.teammachine.staffrostering.repository.ShiftDateRepository;
import com.teammachine.staffrostering.web.rest.dto.ScheduledShiftDTO;
import com.teammachine.staffrostering.web.rest.errors.CustomParameterizedException;
import com.teammachine.staffrostering.web.rest.errors.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing Schedule.
 */
@RestController
@RequestMapping({"/api", "/api_basic"})
public class ScheduleResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleResource.class);

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;

    @Inject
    private ShiftDateRepository shiftDateRepository;

    @RequestMapping(value = "/schedules",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ScheduledShiftDTO> getEmployeeSchedule(@RequestParam(value = "employee") Long employeeId,
                                                       @RequestParam(value = "from") Long fromShiftDate,
                                                       @RequestParam(value = "to") Long toShiftDate) {
        log.debug("REST request to get employee schedule");
        Employee employee = findEmployeeOrThrowException(employeeId);
        ShiftDate from = findShiftDateOrThrowException(fromShiftDate);
        ShiftDate to = findShiftDateOrThrowException(toShiftDate);
        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findForEmployee(employee, from.getDate(), to.getDate());
        if (shiftAssignments.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Shift, List<ShiftAssignment>> relatedShiftAssignments = getShiftAssignmentPerShiftMap(
            shiftAssignments.stream().map(ShiftAssignment::getShift).collect(Collectors.toSet())
        );
        return shiftAssignments.stream()
            .map(shiftAssignment -> createDTO(shiftAssignment, relatedShiftAssignments.get(shiftAssignment.getShift())))
            .sorted(Comparator.comparing(dto -> dto.getShiftDate().getDate()))
            .collect(Collectors.toList());
    }

    private Employee findEmployeeOrThrowException(Long employeeId) {
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null) {
            throw new CustomParameterizedException(ErrorConstants.ERR_NO_SUCH_EMPLOYEE, "" + employeeId);
        }
        return employee;
    }

    private ShiftDate findShiftDateOrThrowException(Long shiftDateId) {
        ShiftDate shiftDate = shiftDateRepository.findOne(shiftDateId);
        if (shiftDate == null) {
            throw new CustomParameterizedException(ErrorConstants.ERR_NO_SUCH_SHIFT_DATE, "" + shiftDateId);
        }
        return shiftDate;
    }

    private Map<Shift, List<ShiftAssignment>> getShiftAssignmentPerShiftMap(Set<Shift> shift) {
        return shiftAssignmentRepository.findAllForShifts(shift).stream()
            .collect(Collectors.toMap(
                ShiftAssignment::getShift,
                Collections::singletonList, (sha1, sha2) -> {
                    List<ShiftAssignment> result = new ArrayList<>(sha1);
                    result.addAll(sha2);
                    return result;
                }));
    }

    private ScheduledShiftDTO createDTO(ShiftAssignment shiftAssignment, List<ShiftAssignment> relatedShiftAssignments) {
        ScheduledShiftDTO dto = new ScheduledShiftDTO();
        dto.setShiftDate(shiftAssignment.getShift().getShiftDate());
        dto.setShiftType(shiftAssignment.getShift().getShiftType());
        dto.setTasks(shiftAssignment.getTaskList());
        if (relatedShiftAssignments != null) {
            Set<Employee> coworkers = relatedShiftAssignments.stream()
                .filter(sha -> !sha.getEmployee().equals(shiftAssignment.getEmployee()))
                .filter(sha -> sha.getTaskList().stream().anyMatch(task -> shiftAssignment.getTaskList().contains(task)))
                .map(ShiftAssignment::getEmployee)
                .collect(Collectors.toSet());
            dto.setCoworkers(coworkers);
        }
        return dto;
    }
}
