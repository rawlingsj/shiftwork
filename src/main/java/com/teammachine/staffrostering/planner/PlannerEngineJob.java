package com.teammachine.staffrostering.planner;

import com.teammachine.staffrostering.domain.enumeration.JobStatus;

public interface PlannerEngineJob {

    String getJobId();

    JobStatus getStatus();

    PlannerEngineJobResult getResult();

}
