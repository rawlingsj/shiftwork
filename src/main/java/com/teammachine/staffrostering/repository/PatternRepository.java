package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.Pattern;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Pattern entity.
 */
public interface PatternRepository extends JpaRepository<Pattern,Long> {

}
