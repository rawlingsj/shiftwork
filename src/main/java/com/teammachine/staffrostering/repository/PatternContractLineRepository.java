package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.PatternContractLine;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PatternContractLine entity.
 */
public interface PatternContractLineRepository extends JpaRepository<PatternContractLine,Long> {

}
