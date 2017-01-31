package com.teammachine.staffrostering.service;

import java.util.List;

import com.teammachine.staffrostering.domain.Employee;

/**
 * Service Interface for managing Employee.
 */
public interface EmployeeService {

	/**
	 * Save a employee.
	 *
	 * @param employee
	 *            the entity to save
	 * @return the persisted entity
	 */
	Employee save(Employee employee);

	/**
	 * Get all the employees.
	 *
	 * @return the list of entities
	 */
	List<Employee> findAll();

	/**
	 * Get the "id" employee.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	Employee findOne(Long id);

	/**
	 * Delete the "id" employee.
	 *
	 * @param id
	 *            the id of the entity
	 */
	void delete(Long id);

	/**
	 * List the employees based on name or code
	 *
	 * @param like
	 *            of type String
	 * @return list of employees
	 */
	List<Employee> findByCodeOrName(String like);
}
