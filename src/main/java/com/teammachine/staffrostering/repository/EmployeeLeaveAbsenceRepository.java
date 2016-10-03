package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.Employee;
import com.teammachine.staffrostering.domain.EmployeeLeaveAbsence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the EmployeeLeaveAbsence entity.
 */
public interface EmployeeLeaveAbsenceRepository extends JpaRepository<EmployeeLeaveAbsence, Long> {

    List<EmployeeLeaveAbsence> findByEmployee(Employee employee);

}
