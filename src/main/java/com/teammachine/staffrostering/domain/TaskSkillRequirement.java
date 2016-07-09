package com.teammachine.staffrostering.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TaskSkillRequirement.
 */
@Entity
@Table(name = "task_skill_requirement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaskSkillRequirement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    @JsonIgnore
    private Task task;

    @ManyToOne
    @JsonIgnore
    private Skill skill;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("task")
    public EntityRefInfo getTask() {
        return task != null ? new EntityRefInfo(task.getId(), task.getCode()) : null;
    }

    @JsonProperty("task")
    public void setTask(Task task) {
        this.task = task;
    }

    @JsonProperty("skill")
    public EntityRefInfo getSkill() {
        return skill != null ? new EntityRefInfo(skill.getId(), skill.getCode()) : null;
    }

    @JsonProperty("skill")
    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskSkillRequirement taskSkillRequirement = (TaskSkillRequirement) o;
        if(taskSkillRequirement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, taskSkillRequirement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TaskSkillRequirement{" +
            "id=" + id +
            '}';
    }
}
