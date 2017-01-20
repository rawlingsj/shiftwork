package com.teammachine.staffrostering.domain.enumeration;

/**
 * The JobStatus enumeration.
 */
public enum JobStatus {
    PENDING("Pending"),RUNNING("Running"),COMPLETED("Completed");

    private String description;

    JobStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
