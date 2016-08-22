package com.teammachine.staffrostering.planner;

import com.teammachine.staffrostering.domain.enumeration.JobStatus;

public interface PlannerServiceJob {

    String getJobId();

    JobStatus getStatus();

    PlannerServiceJobResult getResult();

}
