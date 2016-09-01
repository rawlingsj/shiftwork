package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.EmployeeAbsentReason;
import com.teammachine.staffrostering.domain.EmployeeLeaveAbsence;
import com.teammachine.staffrostering.domain.enumeration.DurationUnit;
import com.teammachine.staffrostering.repository.EmployeeLeaveAbsenceRepository;
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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EmployeeLeaveAbsence.
 */
@RestController
@RequestMapping({"/api", "/api_basic"})
public class EmployeeLeaveAbsenceResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeLeaveAbsenceResource.class);

    @Inject
    private EmployeeLeaveAbsenceRepository employeeLeaveAbsenceRepository;

    /**
     * POST  /employee-leave-absences : Create a new employeeLeaveAbsence.
     *
     * @param employeeLeaveAbsence the employeeLeaveAbsence to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employeeLeaveAbsence, or with status 400 (Bad Request) if the employeeLeaveAbsence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employee-leave-absences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeLeaveAbsence> createEmployeeLeaveAbsence(@RequestBody EmployeeLeaveAbsence employeeLeaveAbsence) throws URISyntaxException {
        log.debug("REST request to save EmployeeLeaveAbsence : {}", employeeLeaveAbsence);
        if (employeeLeaveAbsence.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employeeLeaveAbsence", "idexists", "A new employeeLeaveAbsence cannot already have an ID")).body(null);
        }
        if (employeeLeaveAbsence.getAbsentFrom() != null
            && employeeLeaveAbsence.getAbsentTo() == null
            && employeeLeaveAbsence.getReason() != null) {
            employeeLeaveAbsence.setAbsentTo(getAbsentToDateFromDefaultPeriodOfReason(employeeLeaveAbsence.getAbsentFrom(), employeeLeaveAbsence.getReason()));
        }
        EmployeeLeaveAbsence result = employeeLeaveAbsenceRepository.save(employeeLeaveAbsence);
        return ResponseEntity.created(new URI("/api/employee-leave-absences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("employeeLeaveAbsence", result.getId().toString()))
            .body(result);
    }

    private ZonedDateTime getAbsentToDateFromDefaultPeriodOfReason(ZonedDateTime absentFrom, EmployeeAbsentReason reason) {
        Integer amount = reason.getDefaultDuration();
        DurationUnit durationUnit = reason.getDefaultDurationUnit();
        if (amount != null && durationUnit != null) {
            return absentFrom.plus(amount, durationUnit.getTemporalUnit());
        } else {
            return null;
        }
    }

    /**
     * PUT  /employee-leave-absences : Updates an existing employeeLeaveAbsence.
     *
     * @param employeeLeaveAbsence the employeeLeaveAbsence to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employeeLeaveAbsence,
     * or with status 400 (Bad Request) if the employeeLeaveAbsence is not valid,
     * or with status 500 (Internal Server Error) if the employeeLeaveAbsence couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employee-leave-absences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeLeaveAbsence> updateEmployeeLeaveAbsence(@RequestBody EmployeeLeaveAbsence employeeLeaveAbsence) throws URISyntaxException {
        log.debug("REST request to update EmployeeLeaveAbsence : {}", employeeLeaveAbsence);
        if (employeeLeaveAbsence.getId() == null) {
            return createEmployeeLeaveAbsence(employeeLeaveAbsence);
        }
        if (employeeLeaveAbsence.getAbsentFrom() != null
            && employeeLeaveAbsence.getAbsentTo() == null
            && employeeLeaveAbsence.getReason() != null) {
            employeeLeaveAbsence.setAbsentTo(getAbsentToDateFromDefaultPeriodOfReason(employeeLeaveAbsence.getAbsentFrom(), employeeLeaveAbsence.getReason()));
        }
        EmployeeLeaveAbsence result = employeeLeaveAbsenceRepository.save(employeeLeaveAbsence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employeeLeaveAbsence", employeeLeaveAbsence.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employee-leave-absences : get all the employeeLeaveAbsences.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of employeeLeaveAbsences in body
     */
    @RequestMapping(value = "/employee-leave-absences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeLeaveAbsence> getAllEmployeeLeaveAbsences() {
        log.debug("REST request to get all EmployeeLeaveAbsences");
        List<EmployeeLeaveAbsence> employeeLeaveAbsences = employeeLeaveAbsenceRepository.findAll();
        return employeeLeaveAbsences;
    }

    /**
     * GET  /employee-leave-absences/:id : get the "id" employeeLeaveAbsence.
     *
     * @param id the id of the employeeLeaveAbsence to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeLeaveAbsence, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/employee-leave-absences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeLeaveAbsence> getEmployeeLeaveAbsence(@PathVariable Long id) {
        log.debug("REST request to get EmployeeLeaveAbsence : {}", id);
        EmployeeLeaveAbsence employeeLeaveAbsence = employeeLeaveAbsenceRepository.findOne(id);
        return Optional.ofNullable(employeeLeaveAbsence)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employee-leave-absences/:id : delete the "id" employeeLeaveAbsence.
     *
     * @param id the id of the employeeLeaveAbsence to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/employee-leave-absences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployeeLeaveAbsence(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeLeaveAbsence : {}", id);
        employeeLeaveAbsenceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employeeLeaveAbsence", id.toString())).build();
    }

}
