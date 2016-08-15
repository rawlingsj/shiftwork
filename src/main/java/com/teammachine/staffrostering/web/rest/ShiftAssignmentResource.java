package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;
import com.teammachine.staffrostering.repository.ShiftDateRepository;
import com.teammachine.staffrostering.web.rest.errors.CustomParameterizedException;
import com.teammachine.staffrostering.web.rest.errors.ErrorConstants;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing ShiftAssignment.
 */
@RestController
@RequestMapping({"/api", "/api_basic"})
public class ShiftAssignmentResource {

    private final Logger log = LoggerFactory.getLogger(ShiftAssignmentResource.class);

    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;
    @Inject
    private ShiftDateRepository shiftDateRepository;

    /**
     * POST  /shift-assignments : Create a new shiftAssignment.
     *
     * @param shiftAssignment the shiftAssignment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shiftAssignment, or with status 400 (Bad Request) if the shiftAssignment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shift-assignments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShiftAssignment> createShiftAssignment(@RequestBody ShiftAssignment shiftAssignment) throws URISyntaxException {
        log.debug("REST request to save ShiftAssignment : {}", shiftAssignment);
        if (shiftAssignment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("shiftAssignment", "idexists", "A new shiftAssignment cannot already have an ID")).body(null);
        }
        ShiftAssignment result = shiftAssignmentRepository.save(shiftAssignment);
        return ResponseEntity.created(new URI("/api/shift-assignments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("shiftAssignment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shift-assignments : Updates an existing shiftAssignment.
     *
     * @param shiftAssignment the shiftAssignment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shiftAssignment,
     * or with status 400 (Bad Request) if the shiftAssignment is not valid,
     * or with status 500 (Internal Server Error) if the shiftAssignment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shift-assignments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShiftAssignment> updateShiftAssignment(@RequestBody ShiftAssignment shiftAssignment) throws URISyntaxException {
        log.debug("REST request to update ShiftAssignment : {}", shiftAssignment);
        if (shiftAssignment.getId() == null) {
            return createShiftAssignment(shiftAssignment);
        }
        ShiftAssignment result = shiftAssignmentRepository.save(shiftAssignment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shiftAssignment", shiftAssignment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shift-assignments : get all the shiftAssignments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of shiftAssignments in body
     */
    @RequestMapping(value = "/shift-assignments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ShiftAssignment> getAllShiftAssignments(@RequestParam(value = "shiftDate", required = false) Long shiftDateId) {
        log.debug("REST request to get ShiftAssignments");
        if (shiftDateId != null) {
            return getShiftAssignmentsForShiftDate(shiftDateId);
        } else {
            return getAllShiftAssignments();
        }
    }

    private List<ShiftAssignment> getAllShiftAssignments() {
        return shiftAssignmentRepository.findAllWithEagerRelationships().stream()
            .sorted(Comparator.comparing(sha -> sha.getShift().getShiftDate().getDayIndex()))
            .collect(Collectors.toList());
    }

    private List<ShiftAssignment> getShiftAssignmentsForShiftDate(long shiftDateId) {
        ShiftDate shiftDate = shiftDateRepository.findOne(shiftDateId);
        if (shiftDate == null) {
            throw new CustomParameterizedException(ErrorConstants.ERR_NO_SUCH_SHIFT_DATE, "" + shiftDateId);
        }
        return shiftAssignmentRepository.findAllForShiftDate(shiftDate).stream()
            .sorted(Comparator.<ShiftAssignment, Integer>comparing(sha -> sha.getShift().getShiftType().getIndex()).thenComparing(ShiftAssignment::getIndexInShift))
            .collect(Collectors.toList());
    }

    /**
     * GET  /shift-assignments/:id : get the "id" shiftAssignment.
     *
     * @param id the id of the shiftAssignment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shiftAssignment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/shift-assignments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShiftAssignment> getShiftAssignment(@PathVariable Long id) {
        log.debug("REST request to get ShiftAssignment : {}", id);
        ShiftAssignment shiftAssignment = shiftAssignmentRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(shiftAssignment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shift-assignments/:id : delete the "id" shiftAssignment.
     *
     * @param id the id of the shiftAssignment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/shift-assignments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShiftAssignment(@PathVariable Long id) {
        log.debug("REST request to delete ShiftAssignment : {}", id);
        shiftAssignmentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shiftAssignment", id.toString())).build();
    }
}
