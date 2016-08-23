package com.teammachine.staffrostering.planner;

import com.teammachine.staffrostering.domain.StaffRosterParametrization;

import java.util.List;
import java.util.Optional;

public interface PlannerEngine {

    List<PlannerEngineJob> getAllPlanningJobs();

    Optional<PlannerEngineJob> getPlanningJob(String jobId);

    Optional<PlannerEngineJob> runPlanningJob(StaffRosterParametrization staffRosterParametrization);

    void terminateAndDeleteJob(String jobId);

}
