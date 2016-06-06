package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.EmployeeShiftOffRequest;
import com.teammachine.staffrostering.repository.EmployeeShiftOffRequestRepository;
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
 * REST controller for managing EmployeeShiftOffRequest.
 */
@RestController
@RequestMapping({"/api", "/api_basic"})
public class EmployeeShiftOffRequestResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeShiftOffRequestResource.class);
        
    @Inject
    private EmployeeShiftOffRequestRepository employeeShiftOffRequestRepository;
    
    /**
     * POST  /employee-shift-off-requests : Create a new employeeShiftOffRequest.
     *
     * @param employeeShiftOffRequest the employeeShiftOffRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employeeShiftOffRequest, or with status 400 (Bad Request) if the employeeShiftOffRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employee-shift-off-requests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeShiftOffRequest> createEmployeeShiftOffRequest(@RequestBody EmployeeShiftOffRequest employeeShiftOffRequest) throws URISyntaxException {
        log.debug("REST request to save EmployeeShiftOffRequest : {}", employeeShiftOffRequest);
        if (employeeShiftOffRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employeeShiftOffRequest", "idexists", "A new employeeShiftOffRequest cannot already have an ID")).body(null);
        }
        EmployeeShiftOffRequest result = employeeShiftOffRequestRepository.save(employeeShiftOffRequest);
        return ResponseEntity.created(new URI("/api/employee-shift-off-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("employeeShiftOffRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employee-shift-off-requests : Updates an existing employeeShiftOffRequest.
     *
     * @param employeeShiftOffRequest the employeeShiftOffRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employeeShiftOffRequest,
     * or with status 400 (Bad Request) if the employeeShiftOffRequest is not valid,
     * or with status 500 (Internal Server Error) if the employeeShiftOffRequest couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employee-shift-off-requests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeShiftOffRequest> updateEmployeeShiftOffRequest(@RequestBody EmployeeShiftOffRequest employeeShiftOffRequest) throws URISyntaxException {
        log.debug("REST request to update EmployeeShiftOffRequest : {}", employeeShiftOffRequest);
        if (employeeShiftOffRequest.getId() == null) {
            return createEmployeeShiftOffRequest(employeeShiftOffRequest);
        }
        EmployeeShiftOffRequest result = employeeShiftOffRequestRepository.save(employeeShiftOffRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employeeShiftOffRequest", employeeShiftOffRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employee-shift-off-requests : get all the employeeShiftOffRequests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of employeeShiftOffRequests in body
     */
    @RequestMapping(value = "/employee-shift-off-requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeShiftOffRequest> getAllEmployeeShiftOffRequests() {
        log.debug("REST request to get all EmployeeShiftOffRequests");
        List<EmployeeShiftOffRequest> employeeShiftOffRequests = employeeShiftOffRequestRepository.findAll();
        return employeeShiftOffRequests;
    }

    /**
     * GET  /employee-shift-off-requests/:id : get the "id" employeeShiftOffRequest.
     *
     * @param id the id of the employeeShiftOffRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeShiftOffRequest, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/employee-shift-off-requests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeShiftOffRequest> getEmployeeShiftOffRequest(@PathVariable Long id) {
        log.debug("REST request to get EmployeeShiftOffRequest : {}", id);
        EmployeeShiftOffRequest employeeShiftOffRequest = employeeShiftOffRequestRepository.findOne(id);
        return Optional.ofNullable(employeeShiftOffRequest)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employee-shift-off-requests/:id : delete the "id" employeeShiftOffRequest.
     *
     * @param id the id of the employeeShiftOffRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/employee-shift-off-requests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployeeShiftOffRequest(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeShiftOffRequest : {}", id);
        employeeShiftOffRequestRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employeeShiftOffRequest", id.toString())).build();
    }

}
