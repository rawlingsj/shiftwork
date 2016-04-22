package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.ShiftType;
import com.teammachine.staffrostering.repository.ShiftTypeRepository;
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
 * REST controller for managing ShiftType.
 */
@RestController
@RequestMapping("/api")
public class ShiftTypeResource {

    private final Logger log = LoggerFactory.getLogger(ShiftTypeResource.class);
        
    @Inject
    private ShiftTypeRepository shiftTypeRepository;
    
    /**
     * POST  /shift-types : Create a new shiftType.
     *
     * @param shiftType the shiftType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shiftType, or with status 400 (Bad Request) if the shiftType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shift-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShiftType> createShiftType(@RequestBody ShiftType shiftType) throws URISyntaxException {
        log.debug("REST request to save ShiftType : {}", shiftType);
        if (shiftType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("shiftType", "idexists", "A new shiftType cannot already have an ID")).body(null);
        }
        ShiftType result = shiftTypeRepository.save(shiftType);
        return ResponseEntity.created(new URI("/api/shift-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("shiftType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shift-types : Updates an existing shiftType.
     *
     * @param shiftType the shiftType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shiftType,
     * or with status 400 (Bad Request) if the shiftType is not valid,
     * or with status 500 (Internal Server Error) if the shiftType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shift-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShiftType> updateShiftType(@RequestBody ShiftType shiftType) throws URISyntaxException {
        log.debug("REST request to update ShiftType : {}", shiftType);
        if (shiftType.getId() == null) {
            return createShiftType(shiftType);
        }
        ShiftType result = shiftTypeRepository.save(shiftType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shiftType", shiftType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shift-types : get all the shiftTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of shiftTypes in body
     */
    @RequestMapping(value = "/shift-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ShiftType> getAllShiftTypes() {
        log.debug("REST request to get all ShiftTypes");
        List<ShiftType> shiftTypes = shiftTypeRepository.findAll();
        return shiftTypes;
    }

    /**
     * GET  /shift-types/:id : get the "id" shiftType.
     *
     * @param id the id of the shiftType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shiftType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/shift-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShiftType> getShiftType(@PathVariable Long id) {
        log.debug("REST request to get ShiftType : {}", id);
        ShiftType shiftType = shiftTypeRepository.findOne(id);
        return Optional.ofNullable(shiftType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shift-types/:id : delete the "id" shiftType.
     *
     * @param id the id of the shiftType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/shift-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShiftType(@PathVariable Long id) {
        log.debug("REST request to delete ShiftType : {}", id);
        shiftTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shiftType", id.toString())).build();
    }

}
