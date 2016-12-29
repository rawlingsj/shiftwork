package com.teammachine.staffrostering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.teammachine.staffrostering.domain.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Employee entity.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    @Query("select employee from Employee employee where employee.active = :active")
    List<Employee> findAllWithStatus(@Param("active") boolean active);

    List<Employee> findAllByCodeLikeIgnoreCaseOrNameLikeIgnoreCase(String code, String name);

}
