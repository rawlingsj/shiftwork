package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.ShiftAssignment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ShiftAssignment entity.
 */
public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment,Long> {

}
