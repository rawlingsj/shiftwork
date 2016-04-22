package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.EmployeeShiftOnRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EmployeeShiftOnRequest entity.
 */
public interface EmployeeShiftOnRequestRepository extends JpaRepository<EmployeeShiftOnRequest,Long> {

}
