package com.teammachine.staffrostering.web.rest.dto;

import com.teammachine.staffrostering.domain.Employee;
import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.domain.ShiftType;
import com.teammachine.staffrostering.domain.Task;

import java.util.Set;

public class ScheduledShiftDTO {

    private ShiftType shiftType;

    private ShiftDate shiftDate;

    private Set<Task> tasks;

    private Set<Employee> coworkers;

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

    public Set<Employee> getCoworkers() {
        return coworkers;
    }

    public void setCoworkers(Set<Employee> coworkers) {
        this.coworkers = coworkers;
    }
}
