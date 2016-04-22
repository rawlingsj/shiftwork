package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.SkillProficiency;
import com.teammachine.staffrostering.repository.SkillProficiencyRepository;
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
 * REST controller for managing SkillProficiency.
 */
@RestController
@RequestMapping("/api")
public class SkillProficiencyResource {

    private final Logger log = LoggerFactory.getLogger(SkillProficiencyResource.class);
        
    @Inject
    private SkillProficiencyRepository skillProficiencyRepository;
    
    /**
     * POST  /skill-proficiencies : Create a new skillProficiency.
     *
     * @param skillProficiency the skillProficiency to create
     * @return the ResponseEntity with status 201 (Created) and with body the new skillProficiency, or with status 400 (Bad Request) if the skillProficiency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/skill-proficiencies",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SkillProficiency> createSkillProficiency(@RequestBody SkillProficiency skillProficiency) throws URISyntaxException {
        log.debug("REST request to save SkillProficiency : {}", skillProficiency);
        if (skillProficiency.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("skillProficiency", "idexists", "A new skillProficiency cannot already have an ID")).body(null);
        }
        SkillProficiency result = skillProficiencyRepository.save(skillProficiency);
        return ResponseEntity.created(new URI("/api/skill-proficiencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("skillProficiency", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /skill-proficiencies : Updates an existing skillProficiency.
     *
     * @param skillProficiency the skillProficiency to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated skillProficiency,
     * or with status 400 (Bad Request) if the skillProficiency is not valid,
     * or with status 500 (Internal Server Error) if the skillProficiency couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/skill-proficiencies",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SkillProficiency> updateSkillProficiency(@RequestBody SkillProficiency skillProficiency) throws URISyntaxException {
        log.debug("REST request to update SkillProficiency : {}", skillProficiency);
        if (skillProficiency.getId() == null) {
            return createSkillProficiency(skillProficiency);
        }
        SkillProficiency result = skillProficiencyRepository.save(skillProficiency);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("skillProficiency", skillProficiency.getId().toString()))
            .body(result);
    }

    /**
     * GET  /skill-proficiencies : get all the skillProficiencies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of skillProficiencies in body
     */
    @RequestMapping(value = "/skill-proficiencies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SkillProficiency> getAllSkillProficiencies() {
        log.debug("REST request to get all SkillProficiencies");
        List<SkillProficiency> skillProficiencies = skillProficiencyRepository.findAll();
        return skillProficiencies;
    }

    /**
     * GET  /skill-proficiencies/:id : get the "id" skillProficiency.
     *
     * @param id the id of the skillProficiency to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the skillProficiency, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/skill-proficiencies/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SkillProficiency> getSkillProficiency(@PathVariable Long id) {
        log.debug("REST request to get SkillProficiency : {}", id);
        SkillProficiency skillProficiency = skillProficiencyRepository.findOne(id);
        return Optional.ofNullable(skillProficiency)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /skill-proficiencies/:id : delete the "id" skillProficiency.
     *
     * @param id the id of the skillProficiency to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/skill-proficiencies/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSkillProficiency(@PathVariable Long id) {
        log.debug("REST request to delete SkillProficiency : {}", id);
        skillProficiencyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("skillProficiency", id.toString())).build();
    }

}
