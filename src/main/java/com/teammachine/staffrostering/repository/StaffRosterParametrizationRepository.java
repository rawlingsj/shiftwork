package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.StaffRosterParametrization;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StaffRosterParametrization entity.
 */
public interface StaffRosterParametrizationRepository extends JpaRepository<StaffRosterParametrization,Long> {

}
