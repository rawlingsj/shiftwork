package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.SkillProficiency;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SkillProficiency entity.
 */
public interface SkillProficiencyRepository extends JpaRepository<SkillProficiency,Long> {

}
