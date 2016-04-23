package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.MinMaxContractLine;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MinMaxContractLine entity.
 */
public interface MinMaxContractLineRepository extends JpaRepository<MinMaxContractLine,Long> {

}
