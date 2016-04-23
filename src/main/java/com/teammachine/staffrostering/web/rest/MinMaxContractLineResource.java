package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.MinMaxContractLine;
import com.teammachine.staffrostering.repository.MinMaxContractLineRepository;
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
 * REST controller for managing MinMaxContractLine.
 */
@RestController
@RequestMapping("/api")
public class MinMaxContractLineResource {

    private final Logger log = LoggerFactory.getLogger(MinMaxContractLineResource.class);
        
    @Inject
    private MinMaxContractLineRepository minMaxContractLineRepository;
    
    /**
     * POST  /min-max-contract-lines : Create a new minMaxContractLine.
     *
     * @param minMaxContractLine the minMaxContractLine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new minMaxContractLine, or with status 400 (Bad Request) if the minMaxContractLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/min-max-contract-lines",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MinMaxContractLine> createMinMaxContractLine(@RequestBody MinMaxContractLine minMaxContractLine) throws URISyntaxException {
        log.debug("REST request to save MinMaxContractLine : {}", minMaxContractLine);
        if (minMaxContractLine.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("minMaxContractLine", "idexists", "A new minMaxContractLine cannot already have an ID")).body(null);
        }
        MinMaxContractLine result = minMaxContractLineRepository.save(minMaxContractLine);
        return ResponseEntity.created(new URI("/api/min-max-contract-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("minMaxContractLine", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /min-max-contract-lines : Updates an existing minMaxContractLine.
     *
     * @param minMaxContractLine the minMaxContractLine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated minMaxContractLine,
     * or with status 400 (Bad Request) if the minMaxContractLine is not valid,
     * or with status 500 (Internal Server Error) if the minMaxContractLine couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/min-max-contract-lines",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MinMaxContractLine> updateMinMaxContractLine(@RequestBody MinMaxContractLine minMaxContractLine) throws URISyntaxException {
        log.debug("REST request to update MinMaxContractLine : {}", minMaxContractLine);
        if (minMaxContractLine.getId() == null) {
            return createMinMaxContractLine(minMaxContractLine);
        }
        MinMaxContractLine result = minMaxContractLineRepository.save(minMaxContractLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("minMaxContractLine", minMaxContractLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /min-max-contract-lines : get all the minMaxContractLines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of minMaxContractLines in body
     */
    @RequestMapping(value = "/min-max-contract-lines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MinMaxContractLine> getAllMinMaxContractLines() {
        log.debug("REST request to get all MinMaxContractLines");
        List<MinMaxContractLine> minMaxContractLines = minMaxContractLineRepository.findAll();
        return minMaxContractLines;
    }

    /**
     * GET  /min-max-contract-lines/:id : get the "id" minMaxContractLine.
     *
     * @param id the id of the minMaxContractLine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the minMaxContractLine, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/min-max-contract-lines/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MinMaxContractLine> getMinMaxContractLine(@PathVariable Long id) {
        log.debug("REST request to get MinMaxContractLine : {}", id);
        MinMaxContractLine minMaxContractLine = minMaxContractLineRepository.findOne(id);
        return Optional.ofNullable(minMaxContractLine)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /min-max-contract-lines/:id : delete the "id" minMaxContractLine.
     *
     * @param id the id of the minMaxContractLine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/min-max-contract-lines/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMinMaxContractLine(@PathVariable Long id) {
        log.debug("REST request to delete MinMaxContractLine : {}", id);
        minMaxContractLineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("minMaxContractLine", id.toString())).build();
    }

}
