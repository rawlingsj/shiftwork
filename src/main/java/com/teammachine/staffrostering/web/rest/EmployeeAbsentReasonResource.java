package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.EmployeeAbsentReason;
import com.teammachine.staffrostering.repository.EmployeeAbsentReasonRepository;
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
 * REST controller for managing EmployeeAbsentReason.
 */
@RestController
@RequestMapping("/api")
public class EmployeeAbsentReasonResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeAbsentReasonResource.class);
        
    @Inject
    private EmployeeAbsentReasonRepository employeeAbsentReasonRepository;
    
    /**
     * POST  /employee-absent-reasons : Create a new employeeAbsentReason.
     *
     * @param employeeAbsentReason the employeeAbsentReason to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employeeAbsentReason, or with status 400 (Bad Request) if the employeeAbsentReason has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employee-absent-reasons",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeAbsentReason> createEmployeeAbsentReason(@RequestBody EmployeeAbsentReason employeeAbsentReason) throws URISyntaxException {
        log.debug("REST request to save EmployeeAbsentReason : {}", employeeAbsentReason);
        if (employeeAbsentReason.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employeeAbsentReason", "idexists", "A new employeeAbsentReason cannot already have an ID")).body(null);
        }
        EmployeeAbsentReason result = employeeAbsentReasonRepository.save(employeeAbsentReason);
        return ResponseEntity.created(new URI("/api/employee-absent-reasons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("employeeAbsentReason", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employee-absent-reasons : Updates an existing employeeAbsentReason.
     *
     * @param employeeAbsentReason the employeeAbsentReason to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employeeAbsentReason,
     * or with status 400 (Bad Request) if the employeeAbsentReason is not valid,
     * or with status 500 (Internal Server Error) if the employeeAbsentReason couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employee-absent-reasons",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeAbsentReason> updateEmployeeAbsentReason(@RequestBody EmployeeAbsentReason employeeAbsentReason) throws URISyntaxException {
        log.debug("REST request to update EmployeeAbsentReason : {}", employeeAbsentReason);
        if (employeeAbsentReason.getId() == null) {
            return createEmployeeAbsentReason(employeeAbsentReason);
        }
        EmployeeAbsentReason result = employeeAbsentReasonRepository.save(employeeAbsentReason);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employeeAbsentReason", employeeAbsentReason.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employee-absent-reasons : get all the employeeAbsentReasons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of employeeAbsentReasons in body
     */
    @RequestMapping(value = "/employee-absent-reasons",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeAbsentReason> getAllEmployeeAbsentReasons() {
        log.debug("REST request to get all EmployeeAbsentReasons");
        List<EmployeeAbsentReason> employeeAbsentReasons = employeeAbsentReasonRepository.findAll();
        return employeeAbsentReasons;
    }

    /**
     * GET  /employee-absent-reasons/:id : get the "id" employeeAbsentReason.
     *
     * @param id the id of the employeeAbsentReason to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeAbsentReason, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/employee-absent-reasons/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeAbsentReason> getEmployeeAbsentReason(@PathVariable Long id) {
        log.debug("REST request to get EmployeeAbsentReason : {}", id);
        EmployeeAbsentReason employeeAbsentReason = employeeAbsentReasonRepository.findOne(id);
        return Optional.ofNullable(employeeAbsentReason)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employee-absent-reasons/:id : delete the "id" employeeAbsentReason.
     *
     * @param id the id of the employeeAbsentReason to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/employee-absent-reasons/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployeeAbsentReason(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeAbsentReason : {}", id);
        employeeAbsentReasonRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employeeAbsentReason", id.toString())).build();
    }

}
