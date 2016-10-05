package com.teammachine.staffrostering.web.rest;


import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.Employee;
import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.repository.EmployeeRepository;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;
import com.teammachine.staffrostering.web.rest.dto.ScheduledShiftDTO;
import com.teammachine.staffrostering.web.rest.dto.ScheduledShiftDTOFactory;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping({"/api", "/api_basic"})
public class EmployeeShiftResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeShiftResource.class);

    @Inject
    private EmployeeRepository employeeRepository;
    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;
    @Inject
    private ScheduledShiftDTOFactory scheduledShiftDTOFactory;

    @RequestMapping(value = "/employeeshifts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ScheduledShiftDTO> getEmployeeShifts(@RequestParam(value = "employee") Long employeeId,
                                                     @RequestParam(value = "fromDate") Long fromDate,
                                                     @RequestParam(value = "toDate") Long toDate) {
        log.debug("REST request to get employee schedule");
        Employee employee = findEmployeeOrThrowException(employeeId);
        LocalDate fromLocalDate = convertToLocalDate(fromDate);
        LocalDate toLocalDate = convertToLocalDate(toDate);
        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findForEmployee(employee, fromLocalDate, toLocalDate);
        if (shiftAssignments.isEmpty()) {
            return Collections.emptyList();
        }
        return scheduledShiftDTOFactory.createScheduledShiftDTOs(shiftAssignments);
    }

    private LocalDate convertToLocalDate(Long epochMilli) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault()).toLocalDate();
    }

    private Employee findEmployeeOrThrowException(Long employeeId) {
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null) {
            throw new CustomParameterizedException(ErrorConstants.ERR_NO_SUCH_EMPLOYEE, "" + employeeId);
        }
        return employee;
    }
}
