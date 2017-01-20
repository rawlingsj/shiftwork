package com.teammachine.staffrostering;

public class JobStatusUpdate {

    private String jobId;
    private String status;
    private Integer hardConstraintMatches;
    private Integer softConstraintMatches;
    private Integer timeMillisSpent;

    public JobStatusUpdate() {
    }

    public JobStatusUpdate(String jobId, String status,
                           Integer hardConstraintMatches, Integer softConstraintMatches, Integer timeMillisSpent
    ) {
        this.jobId = jobId;
        this.status = status;
        this.hardConstraintMatches = hardConstraintMatches;
        this.softConstraintMatches = softConstraintMatches;
        this.timeMillisSpent = timeMillisSpent;
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

    public Integer getTimeMillisSpent() {
        return timeMillisSpent;
    }
}
