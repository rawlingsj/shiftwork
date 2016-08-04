package com.teammachine.staffrostering.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.Employee;
import com.teammachine.staffrostering.domain.filters.EmployeeFilter;
import com.teammachine.staffrostering.service.EmployeeService;

@RestController
@RequestMapping({ "/api", "/api_basic" })
public class FieldResource {

	private final Logger log = LoggerFactory.getLogger(FieldResource.class);

	@Inject
	private EmployeeService employeeService;

	@RequestMapping(value = "/employees/filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<EmployeeFilter>> findByCodeOrName(@RequestParam("like") String like) {

		log.debug("Filter employees by name or code entered");
		List<Employee> employees = new ArrayList<>();

		if (like != null) {
			employees = employeeService.findByCodeOrName(like);

		} else {
			employees = employeeService.findAll();

		}
		if (employees == null || employees.isEmpty()) {
			return new ResponseEntity<List<EmployeeFilter>>(HttpStatus.NO_CONTENT);
		}
		List<EmployeeFilter> employeeFilters = getEmployeeFilterList(employees);

		return Optional.ofNullable(employeeFilters).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

	}

	@RequestMapping(value = "/employees/filter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<EmployeeFilter>> filterEmployees(@RequestBody EmployeeFilter employeeFilter) {

		log.debug("Filter employees method entered");
		List<Employee> employees = new ArrayList<>();

		if (employeeFilter.getName() != null || employeeFilter.getCode() != null) {
			employees = employeeService.filterEmployee(employeeFilter.getEmployee());

		} else {
			employees = employeeService.findAll();

		}
		if (employees == null || employees.isEmpty()) {
			return new ResponseEntity<List<EmployeeFilter>>(HttpStatus.NO_CONTENT);
		}
		List<EmployeeFilter> employeeFilters = getEmployeeFilterList(employees);
		return Optional.ofNullable(employeeFilters).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// Preparing Employee filter list from list of employees
	public List<EmployeeFilter> getEmployeeFilterList(List<Employee> employees) {
		List<EmployeeFilter> employeeFilters = new ArrayList<>();
		if (employees != null && !employees.isEmpty()) {
			for (Employee employee : employees) {
				EmployeeFilter employeeFilterRes = new EmployeeFilter();
				BeanUtils.copyProperties(employee, employeeFilterRes);
				employeeFilters.add(employeeFilterRes);
			}
		}
		return employeeFilters;
	}

}
