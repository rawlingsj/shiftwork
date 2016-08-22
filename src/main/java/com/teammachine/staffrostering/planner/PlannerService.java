package com.teammachine.staffrostering.planner;

import com.teammachine.staffrostering.domain.StaffRosterParametrization;

import java.util.List;
import java.util.Optional;

public interface PlannerService {

    List<PlannerServiceJob> getAllPlanningJobs();

    Optional<PlannerServiceJob> getPlanningJob(String jobId);

    Optional<PlannerServiceJob> runPlanningJob(StaffRosterParametrization staffRosterParametrization);

    void terminateAndDeleteJob(String jobId);

}
