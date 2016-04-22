package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.TaskTpye;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskTpye entity.
 */
public interface TaskTpyeRepository extends JpaRepository<TaskTpye,Long> {

}
