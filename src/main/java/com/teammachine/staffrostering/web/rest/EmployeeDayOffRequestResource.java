package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.EmployeeDayOffRequest;
import com.teammachine.staffrostering.repository.EmployeeDayOffRequestRepository;
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
 * REST controller for managing EmployeeDayOffRequest.
 */
@RestController
@RequestMapping("/api")
public class EmployeeDayOffRequestResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeDayOffRequestResource.class);
        
    @Inject
    private EmployeeDayOffRequestRepository employeeDayOffRequestRepository;
    
    /**
     * POST  /employee-day-off-requests : Create a new employeeDayOffRequest.
     *
     * @param employeeDayOffRequest the employeeDayOffRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employeeDayOffRequest, or with status 400 (Bad Request) if the employeeDayOffRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employee-day-off-requests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDayOffRequest> createEmployeeDayOffRequest(@RequestBody EmployeeDayOffRequest employeeDayOffRequest) throws URISyntaxException {
        log.debug("REST request to save EmployeeDayOffRequest : {}", employeeDayOffRequest);
        if (employeeDayOffRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employeeDayOffRequest", "idexists", "A new employeeDayOffRequest cannot already have an ID")).body(null);
        }
        EmployeeDayOffRequest result = employeeDayOffRequestRepository.save(employeeDayOffRequest);
        return ResponseEntity.created(new URI("/api/employee-day-off-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("employeeDayOffRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employee-day-off-requests : Updates an existing employeeDayOffRequest.
     *
     * @param employeeDayOffRequest the employeeDayOffRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employeeDayOffRequest,
     * or with status 400 (Bad Request) if the employeeDayOffRequest is not valid,
     * or with status 500 (Internal Server Error) if the employeeDayOffRequest couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employee-day-off-requests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDayOffRequest> updateEmployeeDayOffRequest(@RequestBody EmployeeDayOffRequest employeeDayOffRequest) throws URISyntaxException {
        log.debug("REST request to update EmployeeDayOffRequest : {}", employeeDayOffRequest);
        if (employeeDayOffRequest.getId() == null) {
            return createEmployeeDayOffRequest(employeeDayOffRequest);
        }
        EmployeeDayOffRequest result = employeeDayOffRequestRepository.save(employeeDayOffRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employeeDayOffRequest", employeeDayOffRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employee-day-off-requests : get all the employeeDayOffRequests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of employeeDayOffRequests in body
     */
    @RequestMapping(value = "/employee-day-off-requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeDayOffRequest> getAllEmployeeDayOffRequests() {
        log.debug("REST request to get all EmployeeDayOffRequests");
        List<EmployeeDayOffRequest> employeeDayOffRequests = employeeDayOffRequestRepository.findAll();
        return employeeDayOffRequests;
    }

    /**
     * GET  /employee-day-off-requests/:id : get the "id" employeeDayOffRequest.
     *
     * @param id the id of the employeeDayOffRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeDayOffRequest, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/employee-day-off-requests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDayOffRequest> getEmployeeDayOffRequest(@PathVariable Long id) {
        log.debug("REST request to get EmployeeDayOffRequest : {}", id);
        EmployeeDayOffRequest employeeDayOffRequest = employeeDayOffRequestRepository.findOne(id);
        return Optional.ofNullable(employeeDayOffRequest)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employee-day-off-requests/:id : delete the "id" employeeDayOffRequest.
     *
     * @param id the id of the employeeDayOffRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/employee-day-off-requests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployeeDayOffRequest(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeDayOffRequest : {}", id);
        employeeDayOffRequestRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employeeDayOffRequest", id.toString())).build();
    }

}
