package com.teammachine.staffrostering.planner;

import com.teammachine.staffrostering.domain.PlanningJob;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.domain.enumeration.JobStatus;

public interface PlannerService {

    PlanningJob runPlanningJob(StaffRosterParametrization staffRosterParametrization);

    JobStatus getPlanningJobStatus(PlanningJob planningJob);

    void terminateAndDeleteJob(PlanningJob planningJob);

}
