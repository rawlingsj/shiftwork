package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.ShiftType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ShiftType entity.
 */
public interface ShiftTypeRepository extends JpaRepository<ShiftType,Long> {

}
