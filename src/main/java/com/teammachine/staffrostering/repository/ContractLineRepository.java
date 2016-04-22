package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.ContractLine;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ContractLine entity.
 */
public interface ContractLineRepository extends JpaRepository<ContractLine,Long> {

}
