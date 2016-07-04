package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.EmployeeAbsentReason;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the EmployeeAbsentReason entity.
 */
public interface EmployeeAbsentReasonRepository extends JpaRepository<EmployeeAbsentReason,Long> {

}
