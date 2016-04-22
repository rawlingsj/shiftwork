package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.WeekendDefinition;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WeekendDefinition entity.
 */
public interface WeekendDefinitionRepository extends JpaRepository<WeekendDefinition,Long> {

}
