package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.SkillProficiency;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the SkillProficiency entity.
 */
public interface SkillProficiencyRepository extends JpaRepository<SkillProficiency,Long> {

    @Query("select distinct skillProficiency from SkillProficiency skillProficiency left join fetch skillProficiency.skillList")
    List<SkillProficiency> findAllWithEagerRelationships();

    @Query("select skillProficiency from SkillProficiency skillProficiency left join fetch skillProficiency.skillList where skillProficiency.id =:id")
    SkillProficiency findOneWithEagerRelationships(@Param("id") Long id);

}
