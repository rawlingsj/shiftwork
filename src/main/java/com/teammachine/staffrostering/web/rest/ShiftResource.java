package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.Shift;
import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;
import com.teammachine.staffrostering.repository.ShiftRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Shift.
 */
@RestController
@RequestMapping({"/api", "/api_basic"})
public class ShiftResource {

    private final Logger log = LoggerFactory.getLogger(ShiftResource.class);

    @Inject
    private ShiftRepository shiftRepository;

    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;


    /**
     * POST  /shifts : Create a new shift.
     *
     * @param shift the shift to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shift, or with status 400 (Bad Request) if the shift has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shifts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Shift> createShift(@RequestBody Shift shift) throws URISyntaxException {
        log.debug("REST request to save Shift : {}", shift);
        if (shift.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("shift", "idexists", "A new shift cannot already have an ID")).body(null);
        }
        Shift result = shiftRepository.save(shift);
        List<ShiftAssignment> shiftAssignments = createNewShiftAssignments(result);
        shiftAssignmentRepository.save(shiftAssignments);
        return ResponseEntity.created(new URI("/api/shifts/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("shift", result.getId().toString()))
                .body(result);
    }

    private List<ShiftAssignment> createNewShiftAssignments(Shift shift) {
        List<ShiftAssignment> shiftAssignments = new ArrayList<>(shift.getStaffRequired());
        for(int i = 1; i <= shift.getStaffRequired(); i++) {
            ShiftAssignment shiftAssignment = new ShiftAssignment();
            shiftAssignment.setIndexInShift(i);
            shiftAssignment.setShift(shift);
            shiftAssignment.setTaskList(shift.getShiftType().getTasks());
            shiftAssignments.add(shiftAssignment);
        }
        return shiftAssignments;
    }

    /**
     * PUT  /shifts : Updates an existing shift.
     *
     * @param shift the shift to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shift,
     * or with status 400 (Bad Request) if the shift is not valid,
     * or with status 500 (Internal Server Error) if the shift couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shifts",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Shift> updateShift(@RequestBody Shift shift) throws URISyntaxException {
        log.debug("REST request to update Shift : {}", shift);
        if (shift.getId() == null) {
            return createShift(shift);
        }
        Shift result = shiftRepository.save(shift);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("shift", shift.getId().toString()))
                .body(result);
    }

    /**
     * GET  /shifts : get all the shifts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of shifts in body
     */
    @RequestMapping(value = "/shifts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Shift> getAllShifts() {
        log.debug("REST request to get all Shifts");
        List<Shift> shifts = shiftRepository.findAll();
        return shifts;
    }

    /**
     * GET  /shifts/:id : get the "id" shift.
     *
     * @param id the id of the shift to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shift, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/shifts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Shift> getShift(@PathVariable Long id) {
        log.debug("REST request to get Shift : {}", id);
        Shift shift = shiftRepository.findOne(id);
        return Optional.ofNullable(shift)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shifts/:id : delete the "id" shift.
     *
     * @param id the id of the shift to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/shifts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        log.debug("REST request to delete Shift : {}", id);
        shiftRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shift", id.toString())).build();
    }

}
