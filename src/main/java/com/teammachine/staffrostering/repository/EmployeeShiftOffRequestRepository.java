package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.EmployeeShiftOffRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EmployeeShiftOffRequest entity.
 */
public interface EmployeeShiftOffRequestRepository extends JpaRepository<EmployeeShiftOffRequest,Long> {

}
