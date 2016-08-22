package com.teammachine.staffrostering.planner;

import com.teammachine.staffrostering.domain.ShiftAssignment;

import java.util.List;

public interface PlannerServiceJobResult {

    int getHardConstraintMatches();

    int getSoftConstraintMatches();

    List<ShiftAssignment> getShiftAssignments();

}
