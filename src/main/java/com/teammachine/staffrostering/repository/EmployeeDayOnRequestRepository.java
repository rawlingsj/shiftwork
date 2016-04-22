package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.EmployeeDayOnRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EmployeeDayOnRequest entity.
 */
public interface EmployeeDayOnRequestRepository extends JpaRepository<EmployeeDayOnRequest,Long> {

}
