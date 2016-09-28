package com.teammachine.staffrostering.web.rest.dto;

import com.teammachine.staffrostering.domain.*;

import java.util.Set;
import java.util.stream.Collectors;

public class ScheduledShiftDTO {

    private Long id;

    private ShiftType shiftType;

    private ShiftDate shiftDate;

    private Set<Task> tasks;

    private Set<Employee> coworkers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public ShiftDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(ShiftDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<EntityRefInfo> getCoworkers() {
        if (coworkers != null) {
            return coworkers.stream()
                .map(employee -> new EntityRefInfo(employee.getId(), employee.getCode(), employee.getName()))
                .collect(Collectors.toSet());
        }
        return null;
    }

    public void setCoworkers(Set<Employee> coworkers) {
        this.coworkers = coworkers;
    }
}
