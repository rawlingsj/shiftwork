package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.WeekendDefinition;
import com.teammachine.staffrostering.repository.WeekendDefinitionRepository;
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
 * REST controller for managing WeekendDefinition.
 */
@RestController
@RequestMapping("/api")
public class WeekendDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(WeekendDefinitionResource.class);
        
    @Inject
    private WeekendDefinitionRepository weekendDefinitionRepository;
    
    /**
     * POST  /weekend-definitions : Create a new weekendDefinition.
     *
     * @param weekendDefinition the weekendDefinition to create
     * @return the ResponseEntity with status 201 (Created) and with body the new weekendDefinition, or with status 400 (Bad Request) if the weekendDefinition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/weekend-definitions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeekendDefinition> createWeekendDefinition(@RequestBody WeekendDefinition weekendDefinition) throws URISyntaxException {
        log.debug("REST request to save WeekendDefinition : {}", weekendDefinition);
        if (weekendDefinition.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("weekendDefinition", "idexists", "A new weekendDefinition cannot already have an ID")).body(null);
        }
        WeekendDefinition result = weekendDefinitionRepository.save(weekendDefinition);
        return ResponseEntity.created(new URI("/api/weekend-definitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("weekendDefinition", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /weekend-definitions : Updates an existing weekendDefinition.
     *
     * @param weekendDefinition the weekendDefinition to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated weekendDefinition,
     * or with status 400 (Bad Request) if the weekendDefinition is not valid,
     * or with status 500 (Internal Server Error) if the weekendDefinition couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/weekend-definitions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeekendDefinition> updateWeekendDefinition(@RequestBody WeekendDefinition weekendDefinition) throws URISyntaxException {
        log.debug("REST request to update WeekendDefinition : {}", weekendDefinition);
        if (weekendDefinition.getId() == null) {
            return createWeekendDefinition(weekendDefinition);
        }
        WeekendDefinition result = weekendDefinitionRepository.save(weekendDefinition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("weekendDefinition", weekendDefinition.getId().toString()))
            .body(result);
    }

    /**
     * GET  /weekend-definitions : get all the weekendDefinitions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of weekendDefinitions in body
     */
    @RequestMapping(value = "/weekend-definitions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WeekendDefinition> getAllWeekendDefinitions() {
        log.debug("REST request to get all WeekendDefinitions");
        List<WeekendDefinition> weekendDefinitions = weekendDefinitionRepository.findAll();
        return weekendDefinitions;
    }

    /**
     * GET  /weekend-definitions/:id : get the "id" weekendDefinition.
     *
     * @param id the id of the weekendDefinition to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the weekendDefinition, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/weekend-definitions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeekendDefinition> getWeekendDefinition(@PathVariable Long id) {
        log.debug("REST request to get WeekendDefinition : {}", id);
        WeekendDefinition weekendDefinition = weekendDefinitionRepository.findOne(id);
        return Optional.ofNullable(weekendDefinition)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /weekend-definitions/:id : delete the "id" weekendDefinition.
     *
     * @param id the id of the weekendDefinition to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/weekend-definitions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWeekendDefinition(@PathVariable Long id) {
        log.debug("REST request to delete WeekendDefinition : {}", id);
        weekendDefinitionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("weekendDefinition", id.toString())).build();
    }

}
