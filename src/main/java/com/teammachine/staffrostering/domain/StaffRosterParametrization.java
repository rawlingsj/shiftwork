package com.teammachine.staffrostering.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A StaffRosterParametrization.
 */
@Entity
@Table(name = "staff_roster_parametrization")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StaffRosterParametrization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "hard_constraint_matches")
    private Integer hardConstraintMatches;

    @Column(name = "soft_constraint_matches")
    private Integer softConstraintMatches;

    @Column(name = "last_run_time")
    private ZonedDateTime lastRunTime;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "duration")
    private Integer duration;

    @ManyToOne
    private ShiftDate firstShiftDate;

    @ManyToOne
    private ShiftDate lastShiftDate;

    @ManyToOne
    private ShiftDate planningWindowStart;

    @ManyToOne
    private ShiftDate planningWindowEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ZonedDateTime getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(ZonedDateTime lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ShiftDate getFirstShiftDate() {
        return firstShiftDate;
    }

    public void setFirstShiftDate(ShiftDate shiftDate) {
        this.firstShiftDate = shiftDate;
    }

    public ShiftDate getLastShiftDate() {
        return lastShiftDate;
    }

    public void setLastShiftDate(ShiftDate shiftDate) {
        this.lastShiftDate = shiftDate;
    }

    public ShiftDate getPlanningWindowStart() {
        return planningWindowStart;
    }

    public void setPlanningWindowStart(ShiftDate shiftDate) {
        this.planningWindowStart = shiftDate;
    }

    public ShiftDate getPlanningWindowEnd() {
        return planningWindowEnd;
    }

    public void setPlanningWindowEnd(ShiftDate shiftDate) {
        this.planningWindowEnd = shiftDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StaffRosterParametrization staffRosterParametrization = (StaffRosterParametrization) o;
        if(staffRosterParametrization.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, staffRosterParametrization.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StaffRosterParametrization{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", hardConstraintMatches='" + hardConstraintMatches + "'" +
            ", softConstraintMatches='" + softConstraintMatches + "'" +
            ", lastRunTime='" + lastRunTime + "'" +
            ", duration='" + duration + "'" +
            '}';
    }
}
