package com.teammachine.staffrostering.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.teammachine.staffrostering.domain.EntityRefInfo;
import com.teammachine.staffrostering.domain.Task;

import java.util.HashSet;
import java.util.Set;

public class TaskWithCoworkersDTO {

    @JsonUnwrapped
    private Task task;
    private Set<EntityRefInfo> coworkers = new HashSet<>();

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Set<EntityRefInfo> getCoworkers() {
        return coworkers;
    }

    public void setCoworkers(Set<EntityRefInfo> coworkers) {
        this.coworkers = coworkers;
    }

    public void addCoworker(EntityRefInfo employeeRef) {
        this.coworkers.add(employeeRef);
    }
}
