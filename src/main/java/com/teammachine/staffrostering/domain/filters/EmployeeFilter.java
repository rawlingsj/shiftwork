package com.teammachine.staffrostering.domain.filters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.teammachine.staffrostering.domain.Employee;

public class EmployeeFilter extends Employee implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String code;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	// Initiating Employee object from EmpFilter Object
	public Employee getEmployee() {
		Employee employee = new Employee();
		employee.setId(id);
		employee.setName(name);
		employee.setCode(code);
		return employee;
	}

	
	@Override
	public String toString() {
		return "EmployeeFilter [id=" + id + ", name=" + name + ", code=" + code + "]";
	}

}
