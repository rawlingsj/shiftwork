package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.ShiftDate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ShiftDate entity.
 */
public interface ShiftDateRepository extends JpaRepository<ShiftDate,Long> {

    List<ShiftDate> findAllByOrderByDateAsc();
}
