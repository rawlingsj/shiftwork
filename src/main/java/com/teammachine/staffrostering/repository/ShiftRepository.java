package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.Shift;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Shift entity.
 */
public interface ShiftRepository extends JpaRepository<Shift,Long> {

}
