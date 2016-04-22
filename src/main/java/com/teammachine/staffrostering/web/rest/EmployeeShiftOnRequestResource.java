package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.EmployeeShiftOnRequest;
import com.teammachine.staffrostering.repository.EmployeeShiftOnRequestRepository;
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
 * REST controller for managing EmployeeShiftOnRequest.
 */
@RestController
@RequestMapping("/api")
public class EmployeeShiftOnRequestResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeShiftOnRequestResource.class);
        
    @Inject
    private EmployeeShiftOnRequestRepository employeeShiftOnRequestRepository;
    
    /**
     * POST  /employee-shift-on-requests : Create a new employeeShiftOnRequest.
     *
     * @param employeeShiftOnRequest the employeeShiftOnRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employeeShiftOnRequest, or with status 400 (Bad Request) if the employeeShiftOnRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employee-shift-on-requests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeShiftOnRequest> createEmployeeShiftOnRequest(@RequestBody EmployeeShiftOnRequest employeeShiftOnRequest) throws URISyntaxException {
        log.debug("REST request to save EmployeeShiftOnRequest : {}", employeeShiftOnRequest);
        if (employeeShiftOnRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employeeShiftOnRequest", "idexists", "A new employeeShiftOnRequest cannot already have an ID")).body(null);
        }
        EmployeeShiftOnRequest result = employeeShiftOnRequestRepository.save(employeeShiftOnRequest);
        return ResponseEntity.created(new URI("/api/employee-shift-on-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("employeeShiftOnRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employee-shift-on-requests : Updates an existing employeeShiftOnRequest.
     *
     * @param employeeShiftOnRequest the employeeShiftOnRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employeeShiftOnRequest,
     * or with status 400 (Bad Request) if the employeeShiftOnRequest is not valid,
     * or with status 500 (Internal Server Error) if the employeeShiftOnRequest couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employee-shift-on-requests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeShiftOnRequest> updateEmployeeShiftOnRequest(@RequestBody EmployeeShiftOnRequest employeeShiftOnRequest) throws URISyntaxException {
        log.debug("REST request to update EmployeeShiftOnRequest : {}", employeeShiftOnRequest);
        if (employeeShiftOnRequest.getId() == null) {
            return createEmployeeShiftOnRequest(employeeShiftOnRequest);
        }
        EmployeeShiftOnRequest result = employeeShiftOnRequestRepository.save(employeeShiftOnRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employeeShiftOnRequest", employeeShiftOnRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employee-shift-on-requests : get all the employeeShiftOnRequests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of employeeShiftOnRequests in body
     */
    @RequestMapping(value = "/employee-shift-on-requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeShiftOnRequest> getAllEmployeeShiftOnRequests() {
        log.debug("REST request to get all EmployeeShiftOnRequests");
        List<EmployeeShiftOnRequest> employeeShiftOnRequests = employeeShiftOnRequestRepository.findAll();
        return employeeShiftOnRequests;
    }

    /**
     * GET  /employee-shift-on-requests/:id : get the "id" employeeShiftOnRequest.
     *
     * @param id the id of the employeeShiftOnRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeShiftOnRequest, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/employee-shift-on-requests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeShiftOnRequest> getEmployeeShiftOnRequest(@PathVariable Long id) {
        log.debug("REST request to get EmployeeShiftOnRequest : {}", id);
        EmployeeShiftOnRequest employeeShiftOnRequest = employeeShiftOnRequestRepository.findOne(id);
        return Optional.ofNullable(employeeShiftOnRequest)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employee-shift-on-requests/:id : delete the "id" employeeShiftOnRequest.
     *
     * @param id the id of the employeeShiftOnRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/employee-shift-on-requests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployeeShiftOnRequest(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeShiftOnRequest : {}", id);
        employeeShiftOnRequestRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employeeShiftOnRequest", id.toString())).build();
    }

}
