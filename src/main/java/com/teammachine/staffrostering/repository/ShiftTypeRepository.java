package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.ShiftType;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ShiftType entity.
 */
public interface ShiftTypeRepository extends JpaRepository<ShiftType,Long> {

    @Query("select distinct shiftType from ShiftType shiftType left join fetch shiftType.tasks")
    List<ShiftType> findAllWithEagerRelationships();

    @Query("select shiftType from ShiftType shiftType left join fetch shiftType.tasks where shiftType.id =:id")
    ShiftType findOneWithEagerRelationships(@Param("id") Long id);

}
