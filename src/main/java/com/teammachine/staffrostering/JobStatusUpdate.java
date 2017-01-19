package com.teammachine.staffrostering;

import com.teammachine.staffrostering.domain.enumeration.JobStatus;

public class JobStatusUpdate {

    private String jobId;
    private String status;
    private Integer hardConstraintMatches;
    private Integer softConstraintMatches;

    public JobStatusUpdate() {
    }

    public JobStatusUpdate(String jobId, String status, Integer hardConstraintMatches, Integer softConstraintMatches) {
        this.jobId = jobId;
        this.status = status;
        this.hardConstraintMatches = hardConstraintMatches;
        this.softConstraintMatches = softConstraintMatches;
    }



    public String getJobId() {
        return jobId;
    }

    public String getStatus() {
        return status;
    }

    public Integer getHardConstraintMatches() {
        return hardConstraintMatches;
    }

    public Integer getSoftConstraintMatches() {
        return softConstraintMatches;
    }
}
