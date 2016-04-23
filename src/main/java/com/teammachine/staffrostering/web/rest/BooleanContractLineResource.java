package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.BooleanContractLine;
import com.teammachine.staffrostering.repository.BooleanContractLineRepository;
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
 * REST controller for managing BooleanContractLine.
 */
@RestController
@RequestMapping("/api")
public class BooleanContractLineResource {

    private final Logger log = LoggerFactory.getLogger(BooleanContractLineResource.class);
        
    @Inject
    private BooleanContractLineRepository booleanContractLineRepository;
    
    /**
     * POST  /boolean-contract-lines : Create a new booleanContractLine.
     *
     * @param booleanContractLine the booleanContractLine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new booleanContractLine, or with status 400 (Bad Request) if the booleanContractLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/boolean-contract-lines",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BooleanContractLine> createBooleanContractLine(@RequestBody BooleanContractLine booleanContractLine) throws URISyntaxException {
        log.debug("REST request to save BooleanContractLine : {}", booleanContractLine);
        if (booleanContractLine.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("booleanContractLine", "idexists", "A new booleanContractLine cannot already have an ID")).body(null);
        }
        BooleanContractLine result = booleanContractLineRepository.save(booleanContractLine);
        return ResponseEntity.created(new URI("/api/boolean-contract-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("booleanContractLine", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boolean-contract-lines : Updates an existing booleanContractLine.
     *
     * @param booleanContractLine the booleanContractLine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated booleanContractLine,
     * or with status 400 (Bad Request) if the booleanContractLine is not valid,
     * or with status 500 (Internal Server Error) if the booleanContractLine couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/boolean-contract-lines",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BooleanContractLine> updateBooleanContractLine(@RequestBody BooleanContractLine booleanContractLine) throws URISyntaxException {
        log.debug("REST request to update BooleanContractLine : {}", booleanContractLine);
        if (booleanContractLine.getId() == null) {
            return createBooleanContractLine(booleanContractLine);
        }
        BooleanContractLine result = booleanContractLineRepository.save(booleanContractLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("booleanContractLine", booleanContractLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boolean-contract-lines : get all the booleanContractLines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of booleanContractLines in body
     */
    @RequestMapping(value = "/boolean-contract-lines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BooleanContractLine> getAllBooleanContractLines() {
        log.debug("REST request to get all BooleanContractLines");
        List<BooleanContractLine> booleanContractLines = booleanContractLineRepository.findAll();
        return booleanContractLines;
    }

    /**
     * GET  /boolean-contract-lines/:id : get the "id" booleanContractLine.
     *
     * @param id the id of the booleanContractLine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the booleanContractLine, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/boolean-contract-lines/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BooleanContractLine> getBooleanContractLine(@PathVariable Long id) {
        log.debug("REST request to get BooleanContractLine : {}", id);
        BooleanContractLine booleanContractLine = booleanContractLineRepository.findOne(id);
        return Optional.ofNullable(booleanContractLine)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /boolean-contract-lines/:id : delete the "id" booleanContractLine.
     *
     * @param id the id of the booleanContractLine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/boolean-contract-lines/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBooleanContractLine(@PathVariable Long id) {
        log.debug("REST request to delete BooleanContractLine : {}", id);
        booleanContractLineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("booleanContractLine", id.toString())).build();
    }

}
