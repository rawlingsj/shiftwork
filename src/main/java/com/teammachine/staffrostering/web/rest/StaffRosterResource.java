package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;
import com.teammachine.staffrostering.repository.StaffRosterParametrizationRepository;
import com.teammachine.staffrostering.web.rest.dto.StaffRosterDTO;
import com.teammachine.staffrostering.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.time.ZonedDateTime;

@RestController
@RequestMapping({"/api", "/api_basic"})
public class StaffRosterResource {

    private final Logger log = LoggerFactory.getLogger(StaffRosterParametrizationResource.class);

    @Inject
    private StaffRosterParametrizationRepository staffRosterParametrizationRepository;
    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;

    @RequestMapping(value = "/staff-rosters",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity createStaffRoster(@RequestBody StaffRosterDTO staffRosterDTO) {
        log.debug("REST request to create StaffRoster : {}", staffRosterDTO);
        if (staffRosterDTO.getStaffRosterParametrization() == null) {
            return ResponseEntity.badRequest().build();
        }
        staffRosterDTO.getStaffRosterParametrization().setLastRunTime(ZonedDateTime.now());
        staffRosterParametrizationRepository.save(staffRosterDTO.getStaffRosterParametrization());
        shiftAssignmentRepository.save(staffRosterDTO.getShiftAssignments());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("staffRoster", staffRosterDTO.getStaffRosterParametrization().getId().toString()))
            .body(staffRosterDTO);
    }
}
