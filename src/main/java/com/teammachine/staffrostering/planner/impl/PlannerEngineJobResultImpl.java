package com.teammachine.staffrostering.planner.impl;

import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.planner.PlannerEngineJobResult;

import java.util.List;
import java.util.stream.Collectors;

public class PlannerEngineJobResultImpl implements PlannerEngineJobResult {

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
        ShiftDate planningWindowStart = parameterization.getPlanningWindowStart();
        ShiftDate planningWindowEnd = parameterization.getPlanningWindowEnd();
        return shiftAssignments.stream().filter(sa ->
            sa.getShift().getShiftDate().getDayIndex() >= planningWindowStart.getDayIndex()
                && sa.getShift().getShiftDate().getDayIndex() <= planningWindowEnd.getDayIndex()
        ).collect(Collectors.toList());
    }

    public void setShiftAssignments(List<ShiftAssignment> shiftAssignments) {
        this.shiftAssignments = shiftAssignments;
    }
}
