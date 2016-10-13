package com.teammachine.staffrostering.domain;

import com.teammachine.staffrostering.domain.enumeration.TaskImportance;
import com.teammachine.staffrostering.domain.enumeration.TaskType;
import com.teammachine.staffrostering.domain.enumeration.TaskUrgency;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotBlank;

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

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "code validation failed.")   
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

    @Embedded
    private Style style;

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

    public void setStyle(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return style;
    }
}
