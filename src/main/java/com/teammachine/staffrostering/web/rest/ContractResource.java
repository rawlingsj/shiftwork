package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.Contract;
import com.teammachine.staffrostering.repository.ContractRepository;
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
 * REST controller for managing Contract.
 */
@RestController
@RequestMapping({"/api", "/api_basic"})
public class ContractResource {

    private final Logger log = LoggerFactory.getLogger(ContractResource.class);
        
    @Inject
    private ContractRepository contractRepository;
    
    /**
     * POST  /contracts : Create a new contract.
     *
     * @param contract the contract to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contract, or with status 400 (Bad Request) if the contract has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contracts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) throws URISyntaxException {
        log.debug("REST request to save Contract : {}", contract);
        if (contract.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contract", "idexists", "A new contract cannot already have an ID")).body(null);
        }
        Contract result = contractRepository.save(contract);
        return ResponseEntity.created(new URI("/api/contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contract", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contracts : Updates an existing contract.
     *
     * @param contract the contract to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contract,
     * or with status 400 (Bad Request) if the contract is not valid,
     * or with status 500 (Internal Server Error) if the contract couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contracts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contract> updateContract(@RequestBody Contract contract) throws URISyntaxException {
        log.debug("REST request to update Contract : {}", contract);
        if (contract.getId() == null) {
            return createContract(contract);
        }
        Contract result = contractRepository.save(contract);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contract", contract.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contracts : get all the contracts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contracts in body
     */
    @RequestMapping(value = "/contracts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Contract> getAllContracts() {
        log.debug("REST request to get all Contracts");
        List<Contract> contracts = contractRepository.findAll();
        return contracts;
    }

    /**
     * GET  /contracts/:id : get the "id" contract.
     *
     * @param id the id of the contract to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contract, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/contracts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contract> getContract(@PathVariable Long id) {
        log.debug("REST request to get Contract : {}", id);
        Contract contract = contractRepository.findOne(id);
        return Optional.ofNullable(contract)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contracts/:id : delete the "id" contract.
     *
     * @param id the id of the contract to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/contracts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        log.debug("REST request to delete Contract : {}", id);
        contractRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contract", id.toString())).build();
    }

}
