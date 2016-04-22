package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.ContractLine;
import com.teammachine.staffrostering.repository.ContractLineRepository;
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
 * REST controller for managing ContractLine.
 */
@RestController
@RequestMapping("/api")
public class ContractLineResource {

    private final Logger log = LoggerFactory.getLogger(ContractLineResource.class);
        
    @Inject
    private ContractLineRepository contractLineRepository;
    
    /**
     * POST  /contract-lines : Create a new contractLine.
     *
     * @param contractLine the contractLine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contractLine, or with status 400 (Bad Request) if the contractLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contract-lines",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractLine> createContractLine(@RequestBody ContractLine contractLine) throws URISyntaxException {
        log.debug("REST request to save ContractLine : {}", contractLine);
        if (contractLine.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contractLine", "idexists", "A new contractLine cannot already have an ID")).body(null);
        }
        ContractLine result = contractLineRepository.save(contractLine);
        return ResponseEntity.created(new URI("/api/contract-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contractLine", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contract-lines : Updates an existing contractLine.
     *
     * @param contractLine the contractLine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contractLine,
     * or with status 400 (Bad Request) if the contractLine is not valid,
     * or with status 500 (Internal Server Error) if the contractLine couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contract-lines",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractLine> updateContractLine(@RequestBody ContractLine contractLine) throws URISyntaxException {
        log.debug("REST request to update ContractLine : {}", contractLine);
        if (contractLine.getId() == null) {
            return createContractLine(contractLine);
        }
        ContractLine result = contractLineRepository.save(contractLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contractLine", contractLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contract-lines : get all the contractLines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contractLines in body
     */
    @RequestMapping(value = "/contract-lines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ContractLine> getAllContractLines() {
        log.debug("REST request to get all ContractLines");
        List<ContractLine> contractLines = contractLineRepository.findAll();
        return contractLines;
    }

    /**
     * GET  /contract-lines/:id : get the "id" contractLine.
     *
     * @param id the id of the contractLine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contractLine, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/contract-lines/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractLine> getContractLine(@PathVariable Long id) {
        log.debug("REST request to get ContractLine : {}", id);
        ContractLine contractLine = contractLineRepository.findOne(id);
        return Optional.ofNullable(contractLine)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contract-lines/:id : delete the "id" contractLine.
     *
     * @param id the id of the contractLine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/contract-lines/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteContractLine(@PathVariable Long id) {
        log.debug("REST request to delete ContractLine : {}", id);
        contractLineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contractLine", id.toString())).build();
    }

}
