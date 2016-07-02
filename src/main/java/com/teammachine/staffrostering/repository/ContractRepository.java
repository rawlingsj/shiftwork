package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.Contract;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Contract entity.
 */
public interface ContractRepository extends JpaRepository<Contract,Long> {

    @Query("select distinct contract from Contract contract left join fetch contract.weekendDefinitions")
    List<Contract> findAllWithEagerRelationships();

    @Query("select contract from Contract contract left join fetch contract.weekendDefinitions where contract.id =:id")
    Contract findOneWithEagerRelationships(@Param("id") Long id);

}
