package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.Employees;
import com.teammachine.staffrostering.repository.EmployeesRepository;
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
 * REST controller for managing Employees.
 */
@RestController
@RequestMapping("/api")
public class EmployeesResource {

    private final Logger log = LoggerFactory.getLogger(EmployeesResource.class);

    @Inject
    private EmployeesRepository employeesRepository;

    /**
     * POST  /employees2 : Create a new employees.
     *
     * @param employees the employees to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employees, or with status 400 (Bad Request) if the employees has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employees2",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employees> createEmployees(@RequestBody Employees employees) throws URISyntaxException {
        log.debug("REST request to save Employees : {}", employees);
        if (employees.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employees", "idexists", "A new employees cannot already have an ID")).body(null);
        }
        Employees result = employeesRepository.save(employees);
        return ResponseEntity.created(new URI("/api/employees2/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("employees", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employees2 : Updates an existing employees.
     *
     * @param employees the employees to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employees,
     * or with status 400 (Bad Request) if the employees is not valid,
     * or with status 500 (Internal Server Error) if the employees couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employees2",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employees> updateEmployees(@RequestBody Employees employees) throws URISyntaxException {
        log.debug("REST request to update Employees : {}", employees);
        if (employees.getId() == null) {
            return createEmployees(employees);
        }
        Employees result = employeesRepository.save(employees);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employees", employees.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employees2 : get all the employees.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of employees in body
     */
    @RequestMapping(value = "/employees2",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Employees> getAllEmployees() {
        log.debug("REST request to get all Employees");
        List<Employees> employees = employeesRepository.findAll();
        return employees;
    }

    /**
     * GET  /employees2/:id : get the "id" employees.
     *
     * @param id the id of the employees to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employees, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/employees2/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employees> getEmployees(@PathVariable Long id) {
        log.debug("REST request to get Employees : {}", id);
        Employees employees = employeesRepository.findOne(id);
        return Optional.ofNullable(employees)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employees2/:id : delete the "id" employees.
     *
     * @param id the id of the employees to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/employees2/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployees(@PathVariable Long id) {
        log.debug("REST request to delete Employees : {}", id);
        employeesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employees", id.toString())).build();
    }

}
