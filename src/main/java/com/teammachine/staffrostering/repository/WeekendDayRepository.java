package com.teammachine.staffrostering.repository;

import com.teammachine.staffrostering.domain.WeekendDay;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WeekendDay entity.
 */
public interface WeekendDayRepository extends JpaRepository<WeekendDay,Long> {

}
