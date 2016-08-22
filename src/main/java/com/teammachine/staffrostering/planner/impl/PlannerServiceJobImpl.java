package com.teammachine.staffrostering.planner.impl;

import com.teammachine.staffrostering.domain.enumeration.JobStatus;
import com.teammachine.staffrostering.planner.PlannerServiceJob;
import com.teammachine.staffrostering.planner.PlannerServiceJobResult;

public class PlannerServiceJobImpl implements PlannerServiceJob {

    private String jobId;
    private JobStatus status;
    private PlannerServiceJobResultImpl result;

    public String getJobId() {
        return jobId;
    }

    public JobStatus getStatus() {
        return status;
    }

    @Override
    public PlannerServiceJobResult getResult() {
        return result;
    }
}
