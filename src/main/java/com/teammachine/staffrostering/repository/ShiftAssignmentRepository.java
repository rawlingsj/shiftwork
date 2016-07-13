package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.ShiftAssignment;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ShiftAssignment entity.
 */
public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment,Long> {

    @Query("select distinct shiftAssignment from ShiftAssignment shiftAssignment left join fetch shiftAssignment.taskList")
    List<ShiftAssignment> findAllWithEagerRelationships();

    @Query("select shiftAssignment from ShiftAssignment shiftAssignment left join fetch shiftAssignment.taskList where shiftAssignment.id =:id")
    ShiftAssignment findOneWithEagerRelationships(@Param("id") Long id);

}
