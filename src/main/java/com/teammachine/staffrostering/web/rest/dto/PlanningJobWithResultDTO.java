package com.teammachine.staffrostering.web.rest.dto;

import com.teammachine.staffrostering.domain.PlanningJob;
import com.teammachine.staffrostering.domain.ShiftAssignment;

import java.util.List;

public class PlanningJobWithResultDTO extends PlanningJob {

    private List<ShiftAssignment> shiftAssignments;

    public List<ShiftAssignment> getShiftAssignments() {
        return shiftAssignments;
    }

    public void setShiftAssignments(List<ShiftAssignment> shiftAssignments) {
        this.shiftAssignments = shiftAssignments;
    }
}
