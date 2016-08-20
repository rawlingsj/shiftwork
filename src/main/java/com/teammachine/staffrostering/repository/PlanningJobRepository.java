package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.PlanningJob;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PlanningJob entity.
 */
public interface PlanningJobRepository extends JpaRepository<PlanningJob,Long> {

}
