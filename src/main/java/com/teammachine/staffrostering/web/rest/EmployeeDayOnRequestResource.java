package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.EmployeeDayOnRequest;
import com.teammachine.staffrostering.repository.EmployeeDayOnRequestRepository;
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
 * REST controller for managing EmployeeDayOnRequest.
 */
@RestController
@RequestMapping({"/api", "/api_basic"})
public class EmployeeDayOnRequestResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeDayOnRequestResource.class);
        
    @Inject
    private EmployeeDayOnRequestRepository employeeDayOnRequestRepository;
    
    /**
     * POST  /employee-day-on-requests : Create a new employeeDayOnRequest.
     *
     * @param employeeDayOnRequest the employeeDayOnRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employeeDayOnRequest, or with status 400 (Bad Request) if the employeeDayOnRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employee-day-on-requests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDayOnRequest> createEmployeeDayOnRequest(@RequestBody EmployeeDayOnRequest employeeDayOnRequest) throws URISyntaxException {
        log.debug("REST request to save EmployeeDayOnRequest : {}", employeeDayOnRequest);
        if (employeeDayOnRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employeeDayOnRequest", "idexists", "A new employeeDayOnRequest cannot already have an ID")).body(null);
        }
        EmployeeDayOnRequest result = employeeDayOnRequestRepository.save(employeeDayOnRequest);
        return ResponseEntity.created(new URI("/api/employee-day-on-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("employeeDayOnRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employee-day-on-requests : Updates an existing employeeDayOnRequest.
     *
     * @param employeeDayOnRequest the employeeDayOnRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employeeDayOnRequest,
     * or with status 400 (Bad Request) if the employeeDayOnRequest is not valid,
     * or with status 500 (Internal Server Error) if the employeeDayOnRequest couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employee-day-on-requests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDayOnRequest> updateEmployeeDayOnRequest(@RequestBody EmployeeDayOnRequest employeeDayOnRequest) throws URISyntaxException {
        log.debug("REST request to update EmployeeDayOnRequest : {}", employeeDayOnRequest);
        if (employeeDayOnRequest.getId() == null) {
            return createEmployeeDayOnRequest(employeeDayOnRequest);
        }
        EmployeeDayOnRequest result = employeeDayOnRequestRepository.save(employeeDayOnRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employeeDayOnRequest", employeeDayOnRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employee-day-on-requests : get all the employeeDayOnRequests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of employeeDayOnRequests in body
     */
    @RequestMapping(value = "/employee-day-on-requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeDayOnRequest> getAllEmployeeDayOnRequests() {
        log.debug("REST request to get all EmployeeDayOnRequests");
        List<EmployeeDayOnRequest> employeeDayOnRequests = employeeDayOnRequestRepository.findAll();
        return employeeDayOnRequests;
    }

    /**
     * GET  /employee-day-on-requests/:id : get the "id" employeeDayOnRequest.
     *
     * @param id the id of the employeeDayOnRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeDayOnRequest, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/employee-day-on-requests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDayOnRequest> getEmployeeDayOnRequest(@PathVariable Long id) {
        log.debug("REST request to get EmployeeDayOnRequest : {}", id);
        EmployeeDayOnRequest employeeDayOnRequest = employeeDayOnRequestRepository.findOne(id);
        return Optional.ofNullable(employeeDayOnRequest)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employee-day-on-requests/:id : delete the "id" employeeDayOnRequest.
     *
     * @param id the id of the employeeDayOnRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/employee-day-on-requests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployeeDayOnRequest(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeDayOnRequest : {}", id);
        employeeDayOnRequestRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employeeDayOnRequest", id.toString())).build();
    }

}
