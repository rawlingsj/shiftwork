package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.repository.StaffRosterParametrizationRepository;
import com.teammachine.staffrostering.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing StaffRosterParametrization.
 */
@RestController
@RequestMapping({"/api", "/api_basic"})
public class StaffRosterParametrizationResource {

    private final Logger log = LoggerFactory.getLogger(StaffRosterParametrizationResource.class);
        
    @Inject
    private StaffRosterParametrizationRepository staffRosterParametrizationRepository;
    
    /**
     * POST  /staff-roster-parametrizations : Create a new staffRosterParametrization.
     *
     * @param staffRosterParametrization the staffRosterParametrization to create
     * @return the ResponseEntity with status 201 (Created) and with body the new staffRosterParametrization, or with status 400 (Bad Request) if the staffRosterParametrization has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/staff-roster-parametrizations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StaffRosterParametrization> createStaffRosterParametrization(@RequestBody StaffRosterParametrization staffRosterParametrization) throws URISyntaxException {
        log.debug("REST request to save StaffRosterParametrization : {}", staffRosterParametrization);
        if (staffRosterParametrization.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("staffRosterParametrization", "idexists", "A new staffRosterParametrization cannot already have an ID")).body(null);
        }
        StaffRosterParametrization result = staffRosterParametrizationRepository.save(staffRosterParametrization);
        return ResponseEntity.created(new URI("/api/staff-roster-parametrizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("staffRosterParametrization", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /staff-roster-parametrizations : Updates an existing staffRosterParametrization.
     *
     * @param staffRosterParametrization the staffRosterParametrization to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated staffRosterParametrization,
     * or with status 400 (Bad Request) if the staffRosterParametrization is not valid,
     * or with status 500 (Internal Server Error) if the staffRosterParametrization couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/staff-roster-parametrizations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StaffRosterParametrization> updateStaffRosterParametrization(@RequestBody StaffRosterParametrization staffRosterParametrization) throws URISyntaxException {
        log.debug("REST request to update StaffRosterParametrization : {}", staffRosterParametrization);
        if (staffRosterParametrization.getId() == null) {
            return createStaffRosterParametrization(staffRosterParametrization);
        }
        StaffRosterParametrization result = staffRosterParametrizationRepository.save(staffRosterParametrization);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("staffRosterParametrization", staffRosterParametrization.getId().toString()))
            .body(result);
    }

    /**
     * GET  /staff-roster-parametrizations : get all the staffRosterParametrizations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of staffRosterParametrizations in body
     */
    @RequestMapping(value = "/staff-roster-parametrizations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<StaffRosterParametrization> getAllStaffRosterParametrizations() {
        log.debug("REST request to get all StaffRosterParametrizations");
        List<StaffRosterParametrization> staffRosterParametrizations = staffRosterParametrizationRepository.findAll();
        return staffRosterParametrizations;
    }

    /**
     * GET  /staff-roster-parametrizations/:id : get the "id" staffRosterParametrization.
     *
     * @param id the id of the staffRosterParametrization to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the staffRosterParametrization, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/staff-roster-parametrizations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StaffRosterParametrization> getStaffRosterParametrization(@PathVariable Long id) {
        log.debug("REST request to get StaffRosterParametrization : {}", id);
        StaffRosterParametrization staffRosterParametrization = staffRosterParametrizationRepository.findOne(id);
        return Optional.ofNullable(staffRosterParametrization)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /staff-roster-parametrizations/:id : delete the "id" staffRosterParametrization.
     *
     * @param id the id of the staffRosterParametrization to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/staff-roster-parametrizations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStaffRosterParametrization(@PathVariable Long id) {
        log.debug("REST request to delete StaffRosterParametrization : {}", id);
        staffRosterParametrizationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("staffRosterParametrization", id.toString())).build();
    }

}
