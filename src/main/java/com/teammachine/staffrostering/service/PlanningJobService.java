package com.teammachine.staffrostering.service;

import com.teammachine.staffrostering.domain.PlanningJob;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.domain.enumeration.JobStatus;
import com.teammachine.staffrostering.planner.PlannerEngineJobResult;

import java.util.List;
import java.util.Optional;

public interface PlanningJobService {

    List<PlanningJob> findAll();

    Optional<PlanningJob> findById(long id);

    Optional<PlanningJob> runPlanningJob(StaffRosterParametrization parametrization);

    void syncAllPlanningJobsStatuses();

    PlanningJob syncPlanningJobStatus(PlanningJob planningJob);

    void updatePlanningJobStatus(String jobId, JobStatus newStatus, Integer hardConstraintMatches, Integer softConstraintMatches);

    void terminateAndDeleteJob(PlanningJob planningJob);

    Optional<PlannerEngineJobResult> getPlanningJobResult(PlanningJob planningJob);

}
