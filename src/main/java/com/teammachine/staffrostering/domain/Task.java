package com.teammachine.staffrostering.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teammachine.staffrostering.domain.enumeration.TaskImportance;
import com.teammachine.staffrostering.domain.enumeration.TaskType;
import com.teammachine.staffrostering.domain.enumeration.TaskUrgency;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Min(value = 1)
    @Column(name = "staff_needed")
    private Integer staffNeeded;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    @Column(name = "importance")
    private TaskImportance importance;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency")
    private TaskUrgency urgency;

    @ManyToOne
    @JsonIgnore
    private ShiftAssignment shiftAssignment;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStaffNeeded() {
        return staffNeeded;
    }

    public void setStaffNeeded(Integer staffNeeded) {
        this.staffNeeded = staffNeeded;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskImportance getImportance() {
        return importance;
    }

    public void setImportance(TaskImportance importance) {
        this.importance = importance;
    }

    public TaskUrgency getUrgency() {
        return urgency;
    }

    public void setUrgency(TaskUrgency urgency) {
        this.urgency = urgency;
    }

    @JsonProperty("shiftAssignment")
    public EntityRefInfo getShiftAssignment() {
        if (shiftAssignment != null) {
            return new EntityRefInfo(shiftAssignment.getId(), shiftAssignment.getShift().getShiftType().getCode() + "/" + shiftAssignment.getShift().getShiftDate().getDateString());
        } else {
            return null;
        }
    }

    @JsonProperty("shiftAssignment")
    public void setShiftAssignment(ShiftAssignment shiftAssignment) {
        this.shiftAssignment = shiftAssignment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        if(task.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", description='" + description + "'" +
            ", staffNeeded='" + staffNeeded + "'" +
            ", taskType='" + taskType + "'" +
            ", importance='" + importance + "'" +
            ", urgency='" + urgency + "'" +
            '}';
    }
}
