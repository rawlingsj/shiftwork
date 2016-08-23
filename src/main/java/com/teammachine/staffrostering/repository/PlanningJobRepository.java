package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.PlanningJob;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the PlanningJob entity.
 */
public interface PlanningJobRepository extends JpaRepository<PlanningJob, Long> {

    PlanningJob findByJobId(String jobId);

}
