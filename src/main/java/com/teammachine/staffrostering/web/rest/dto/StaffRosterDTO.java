package com.teammachine.staffrostering.web.rest.dto;

import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;

import java.util.List;

public class StaffRosterDTO {

    private StaffRosterParametrization staffRosterParametrization;
    private List<ShiftAssignment> shiftAssignments;

    public StaffRosterParametrization getStaffRosterParametrization() {
        return staffRosterParametrization;
    }

    public List<ShiftAssignment> getShiftAssignments() {
        return shiftAssignments;
    }

    public void setStaffRosterParametrization(StaffRosterParametrization staffRosterParametrization) {
        this.staffRosterParametrization = staffRosterParametrization;
    }

    public void setShiftAssignments(List<ShiftAssignment> shiftAssignments) {
        this.shiftAssignments = shiftAssignments;
    }
}
