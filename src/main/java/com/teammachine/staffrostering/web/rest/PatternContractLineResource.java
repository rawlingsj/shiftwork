package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.PatternContractLine;
import com.teammachine.staffrostering.repository.PatternContractLineRepository;
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
 * REST controller for managing PatternContractLine.
 */
@RestController
@RequestMapping({"/api", "/api_basic"})
public class PatternContractLineResource {

    private final Logger log = LoggerFactory.getLogger(PatternContractLineResource.class);
        
    @Inject
    private PatternContractLineRepository patternContractLineRepository;
    
    /**
     * POST  /pattern-contract-lines : Create a new patternContractLine.
     *
     * @param patternContractLine the patternContractLine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new patternContractLine, or with status 400 (Bad Request) if the patternContractLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pattern-contract-lines",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PatternContractLine> createPatternContractLine(@RequestBody PatternContractLine patternContractLine) throws URISyntaxException {
        log.debug("REST request to save PatternContractLine : {}", patternContractLine);
        if (patternContractLine.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("patternContractLine", "idexists", "A new patternContractLine cannot already have an ID")).body(null);
        }
        PatternContractLine result = patternContractLineRepository.save(patternContractLine);
        return ResponseEntity.created(new URI("/api/pattern-contract-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("patternContractLine", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pattern-contract-lines : Updates an existing patternContractLine.
     *
     * @param patternContractLine the patternContractLine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated patternContractLine,
     * or with status 400 (Bad Request) if the patternContractLine is not valid,
     * or with status 500 (Internal Server Error) if the patternContractLine couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pattern-contract-lines",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PatternContractLine> updatePatternContractLine(@RequestBody PatternContractLine patternContractLine) throws URISyntaxException {
        log.debug("REST request to update PatternContractLine : {}", patternContractLine);
        if (patternContractLine.getId() == null) {
            return createPatternContractLine(patternContractLine);
        }
        PatternContractLine result = patternContractLineRepository.save(patternContractLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("patternContractLine", patternContractLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pattern-contract-lines : get all the patternContractLines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of patternContractLines in body
     */
    @RequestMapping(value = "/pattern-contract-lines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PatternContractLine> getAllPatternContractLines() {
        log.debug("REST request to get all PatternContractLines");
        List<PatternContractLine> patternContractLines = patternContractLineRepository.findAll();
        return patternContractLines;
    }

    /**
     * GET  /pattern-contract-lines/:id : get the "id" patternContractLine.
     *
     * @param id the id of the patternContractLine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the patternContractLine, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pattern-contract-lines/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PatternContractLine> getPatternContractLine(@PathVariable Long id) {
        log.debug("REST request to get PatternContractLine : {}", id);
        PatternContractLine patternContractLine = patternContractLineRepository.findOne(id);
        return Optional.ofNullable(patternContractLine)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pattern-contract-lines/:id : delete the "id" patternContractLine.
     *
     * @param id the id of the patternContractLine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pattern-contract-lines/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePatternContractLine(@PathVariable Long id) {
        log.debug("REST request to delete PatternContractLine : {}", id);
        patternContractLineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("patternContractLine", id.toString())).build();
    }

}
