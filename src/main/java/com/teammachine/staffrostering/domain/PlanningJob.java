package com.teammachine.staffrostering.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.teammachine.staffrostering.domain.enumeration.JobStatus;

/**
 * A PlanningJob.
 */
@Entity
@Table(name = "planning_job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PlanningJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "job_id")
    private String jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private JobStatus status;

    @Column(name = "hard_constraint_matches")
    private Integer hardConstraintMatches;

    @Column(name = "soft_constraint_matches")
    private Integer softConstraintMatches;

    @OneToOne
    @JoinColumn(unique = false)
    private StaffRosterParametrization parameterization;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public Integer getHardConstraintMatches() {
        return hardConstraintMatches;
    }

    public void setHardConstraintMatches(Integer hardConstraintMatches) {
        this.hardConstraintMatches = hardConstraintMatches;
    }

    public Integer getSoftConstraintMatches() {
        return softConstraintMatches;
    }

    public void setSoftConstraintMatches(Integer softConstraintMatches) {
        this.softConstraintMatches = softConstraintMatches;
    }

    public StaffRosterParametrization getParameterization() {
        return parameterization;
    }

    public void setParameterization(StaffRosterParametrization staffRosterParametrization) {
        this.parameterization = staffRosterParametrization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlanningJob planningJob = (PlanningJob) o;
        if(planningJob.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, planningJob.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PlanningJob{" +
            "id=" + id +
            ", jobId='" + jobId + "'" +
            ", status='" + status + "'" +
            ", hardConstraintMatches='" + hardConstraintMatches + "'" +
            ", softConstraintMatches='" + softConstraintMatches + "'" +
            '}';
    }
}
