package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.EmployeeDayOffRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EmployeeDayOffRequest entity.
 */
public interface EmployeeDayOffRequestRepository extends JpaRepository<EmployeeDayOffRequest,Long> {

}
