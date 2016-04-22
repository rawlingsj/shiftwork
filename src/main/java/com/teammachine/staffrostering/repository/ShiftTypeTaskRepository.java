package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.ShiftTypeTask;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ShiftTypeTask entity.
 */
public interface ShiftTypeTaskRepository extends JpaRepository<ShiftTypeTask,Long> {

}
