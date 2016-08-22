package com.teammachine.staffrostering.planner.impl;

import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.planner.PlannerServiceJobResult;

import java.util.List;

public class PlannerServiceJobResultImpl implements PlannerServiceJobResult {

    private StaffRosterParametrization parameterization;
    private List<ShiftAssignment> shiftAssignments;

    @Override
    public int getHardConstraintMatches() {
        return parameterization.getHardConstraintMatches();
    }

    @Override
    public int getSoftConstraintMatches() {
        return parameterization.getSoftConstraintMatches();
    }

    public void setParameterization(StaffRosterParametrization parameterization) {
        this.parameterization = parameterization;
    }

    @Override
    public List<ShiftAssignment> getShiftAssignments() {
        return this.shiftAssignments;
    }

    public void setShiftAssignments(List<ShiftAssignment> shiftAssignments) {
        this.shiftAssignments = shiftAssignments;
    }
}
