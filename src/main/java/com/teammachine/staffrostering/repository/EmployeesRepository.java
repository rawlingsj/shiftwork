package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.Employees;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Employees entity.
 */
public interface EmployeesRepository extends JpaRepository<Employees,Long> {

}
