package com.teammachine.staffrostering.planner;

import com.teammachine.staffrostering.domain.ShiftAssignment;

import java.util.List;

public interface PlannerEngineJobResult {

    int getHardConstraintMatches();

    int getSoftConstraintMatches();

    List<ShiftAssignment> getShiftAssignments();

}
