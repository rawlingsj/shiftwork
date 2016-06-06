package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.WeekendDay;
import com.teammachine.staffrostering.repository.WeekendDayRepository;
import com.teammachine.staffrostering.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WeekendDay.
 */
@RestController
@RequestMapping({"/api", "/api_basic"})
public class WeekendDayResource {

    private final Logger log = LoggerFactory.getLogger(WeekendDayResource.class);
        
    @Inject
    private WeekendDayRepository weekendDayRepository;
    
    /**
     * POST  /weekend-days : Create a new weekendDay.
     *
     * @param weekendDay the weekendDay to create
     * @return the ResponseEntity with status 201 (Created) and with body the new weekendDay, or with status 400 (Bad Request) if the weekendDay has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/weekend-days",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeekendDay> createWeekendDay(@RequestBody WeekendDay weekendDay) throws URISyntaxException {
        log.debug("REST request to save WeekendDay : {}", weekendDay);
        if (weekendDay.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("weekendDay", "idexists", "A new weekendDay cannot already have an ID")).body(null);
        }
        WeekendDay result = weekendDayRepository.save(weekendDay);
        return ResponseEntity.created(new URI("/api/weekend-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("weekendDay", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /weekend-days : Updates an existing weekendDay.
     *
     * @param weekendDay the weekendDay to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated weekendDay,
     * or with status 400 (Bad Request) if the weekendDay is not valid,
     * or with status 500 (Internal Server Error) if the weekendDay couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/weekend-days",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeekendDay> updateWeekendDay(@RequestBody WeekendDay weekendDay) throws URISyntaxException {
        log.debug("REST request to update WeekendDay : {}", weekendDay);
        if (weekendDay.getId() == null) {
            return createWeekendDay(weekendDay);
        }
        WeekendDay result = weekendDayRepository.save(weekendDay);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("weekendDay", weekendDay.getId().toString()))
            .body(result);
    }

    /**
     * GET  /weekend-days : get all the weekendDays.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of weekendDays in body
     */
    @RequestMapping(value = "/weekend-days",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WeekendDay> getAllWeekendDays() {
        log.debug("REST request to get all WeekendDays");
        List<WeekendDay> weekendDays = weekendDayRepository.findAll();
        return weekendDays;
    }

    /**
     * GET  /weekend-days/:id : get the "id" weekendDay.
     *
     * @param id the id of the weekendDay to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the weekendDay, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/weekend-days/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeekendDay> getWeekendDay(@PathVariable Long id) {
        log.debug("REST request to get WeekendDay : {}", id);
        WeekendDay weekendDay = weekendDayRepository.findOne(id);
        return Optional.ofNullable(weekendDay)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /weekend-days/:id : delete the "id" weekendDay.
     *
     * @param id the id of the weekendDay to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/weekend-days/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWeekendDay(@PathVariable Long id) {
        log.debug("REST request to delete WeekendDay : {}", id);
        weekendDayRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("weekendDay", id.toString())).build();
    }

}
