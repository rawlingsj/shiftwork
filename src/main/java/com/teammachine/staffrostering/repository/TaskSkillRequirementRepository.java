package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.TaskSkillRequirement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskSkillRequirement entity.
 */
public interface TaskSkillRequirementRepository extends JpaRepository<TaskSkillRequirement,Long> {

}
