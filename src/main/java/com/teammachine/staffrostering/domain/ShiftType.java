package com.teammachine.staffrostering.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A ShiftType.
 */
@Entity
@Table(name = "shift_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ShiftType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "index")
    private Integer index;

    @Column(name = "description")
    private String description;

    @Column(name = "night_shift")
    private Boolean nightShift;

    @Pattern(regexp = "^([0-1]?[0-9]|[2][0-3]):([0-5][0-9])(:[0-5][0-9])?$")
    @Column(name = "start_time")
    private String startTime;

    @Pattern(regexp = "^([0-1]?[0-9]|[2][0-3]):([0-5][0-9])(:[0-5][0-9])?$")
    @Column(name = "end_time")
    private String endTime;

    @OneToMany(mappedBy = "shiftType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ShiftTypeTask> tasks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isNightShift() {
        return nightShift;
    }

    public void setNightShift(Boolean nightShift) {
        this.nightShift = nightShift;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTimeString() {
        return startTime;
    }

    public String getEndTimeString() {
        return endTime;
    }

    public Set<ShiftTypeTask> getTasks() {
        return tasks;
    }

    public void setTasks(Set<ShiftTypeTask> shiftTypeTasks) {
        this.tasks = shiftTypeTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShiftType shiftType = (ShiftType) o;
        if (shiftType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, shiftType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ShiftType{" +
                "id=" + id +
                ", code='" + code + "'" +
                ", index='" + index + "'" +
                ", description='" + description + "'" +
                ", nightShift='" + nightShift + "'" +
                ", startTime='" + startTime + "'" +
                ", endTime='" + endTime + "'" +
                '}';
    }
}
