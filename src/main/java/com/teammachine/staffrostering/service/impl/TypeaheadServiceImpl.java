package com.teammachine.staffrostering.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teammachine.staffrostering.domain.Employee;
import com.teammachine.staffrostering.domain.dto.EmployeeDTO;
import com.teammachine.staffrostering.service.EmployeeService;
import com.teammachine.staffrostering.service.TypeaheadService;

@Service
@Transactional
public class TypeaheadServiceImpl implements TypeaheadService {

	@Autowired
	private EmployeeService employeeService;

	public List<EmployeeDTO> processTypeaheadReqOfEmployee(String like) {

		List<Employee> employees = new ArrayList<>();
		if (like != null) {
			employees = employeeService.findByCodeOrName(like);
		} else {
			employees = employeeService.findAll();
		}
		Collections.sort(employees, (e1, e2) -> e1.getName().toLowerCase().compareTo(e2.getName().toLowerCase()));
		List<EmployeeDTO> employeeDTOs = getEmployeeDTOList(employees);
		return employeeDTOs;
	}

	// Preparing Employee Dto's list from list of employees
	public List<EmployeeDTO> getEmployeeDTOList(List<Employee> employees) {
		List<EmployeeDTO> employeeDtos = new ArrayList<>();
		if (employees != null && !employees.isEmpty()) {
			for (Employee employee : employees) {
				EmployeeDTO employeeFilterRes = new EmployeeDTO();
				BeanUtils.copyProperties(employee, employeeFilterRes);
				employeeDtos.add(employeeFilterRes);
			}
		}
		return employeeDtos;
	}

}
