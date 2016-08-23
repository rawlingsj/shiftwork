package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.Employee;
import com.teammachine.staffrostering.domain.Shift;
import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.domain.ShiftDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the ShiftAssignment entity.
 */
public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment, Long> {

    @Query("select distinct shiftAssignment from ShiftAssignment shiftAssignment left join fetch shiftAssignment.taskList")
    List<ShiftAssignment> findAllWithEagerRelationships();

    @Query("select distinct shiftAssignment from ShiftAssignment shiftAssignment left join fetch shiftAssignment.taskList where shiftAssignment.id =:id")
    ShiftAssignment findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select distinct shiftAssignment from ShiftAssignment shiftAssignment left join fetch shiftAssignment.taskList " +
        "where shiftAssignment.shift.shiftDate.dayIndex between :fromShiftDateIndex and :toShiftDateIndex")
    List<ShiftAssignment> findAllBetweenShiftDates(@Param("fromShiftDateIndex") Integer fromShiftDateIndex, @Param("toShiftDateIndex") Integer toShiftDateIndex);

    @Query("select distinct shiftAssignment from ShiftAssignment shiftAssignment left join fetch shiftAssignment.taskList " +
        "where shiftAssignment.shift in (:shifts)")
    List<ShiftAssignment> findAllForShifts(@Param("shifts") Set<Shift> shifts);

    @Query("select distinct shiftAssignment from ShiftAssignment shiftAssignment left join fetch shiftAssignment.taskList " +
        "where shiftAssignment.employee = :employee and shiftAssignment.shift.shiftDate.date between :fromDate and :toDate")
    List<ShiftAssignment> findForEmployee(@Param("employee") Employee employee, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}
