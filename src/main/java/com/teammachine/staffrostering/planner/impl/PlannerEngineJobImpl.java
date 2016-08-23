package com.teammachine.staffrostering.planner.impl;

import com.teammachine.staffrostering.domain.enumeration.JobStatus;
import com.teammachine.staffrostering.planner.PlannerEngineJob;
import com.teammachine.staffrostering.planner.PlannerEngineJobResult;

public class PlannerEngineJobImpl implements PlannerEngineJob {

    private String jobId;
    private JobStatus status;
    private PlannerEngineJobResultImpl result;

    public String getJobId() {
        return jobId;
    }

    public JobStatus getStatus() {
        return status;
    }

    @Override
    public PlannerEngineJobResult getResult() {
        return result;
    }
}
