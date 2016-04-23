package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.BooleanContractLine;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BooleanContractLine entity.
 */
public interface BooleanContractLineRepository extends JpaRepository<BooleanContractLine,Long> {

}
