package com.teammachine.staffrostering.web.rest.dto;

import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.domain.ShiftType;

import java.util.List;

public class ScheduledShiftDTO {

    private Long id;

    private ShiftType shiftType;

    private ShiftDate shiftDate;

    private List<TaskWithCoworkersDTO> tasks;

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

    public List<TaskWithCoworkersDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskWithCoworkersDTO> tasks) {
        this.tasks = tasks;
    }

}
