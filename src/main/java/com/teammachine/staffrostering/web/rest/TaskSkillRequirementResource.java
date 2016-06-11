package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.TaskSkillRequirement;
import com.teammachine.staffrostering.repository.TaskSkillRequirementRepository;
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
 * REST controller for managing TaskSkillRequirement.
 */
@RestController
@RequestMapping({"/api", "/api_basic"})
public class TaskSkillRequirementResource {

    private final Logger log = LoggerFactory.getLogger(TaskSkillRequirementResource.class);
        
    @Inject
    private TaskSkillRequirementRepository taskSkillRequirementRepository;
    
    /**
     * POST  /task-skill-requirements : Create a new taskSkillRequirement.
     *
     * @param taskSkillRequirement the taskSkillRequirement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskSkillRequirement, or with status 400 (Bad Request) if the taskSkillRequirement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/task-skill-requirements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskSkillRequirement> createTaskSkillRequirement(@RequestBody TaskSkillRequirement taskSkillRequirement) throws URISyntaxException {
        log.debug("REST request to save TaskSkillRequirement : {}", taskSkillRequirement);
        if (taskSkillRequirement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("taskSkillRequirement", "idexists", "A new taskSkillRequirement cannot already have an ID")).body(null);
        }
        TaskSkillRequirement result = taskSkillRequirementRepository.save(taskSkillRequirement);
        return ResponseEntity.created(new URI("/api/task-skill-requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("taskSkillRequirement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /task-skill-requirements : Updates an existing taskSkillRequirement.
     *
     * @param taskSkillRequirement the taskSkillRequirement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskSkillRequirement,
     * or with status 400 (Bad Request) if the taskSkillRequirement is not valid,
     * or with status 500 (Internal Server Error) if the taskSkillRequirement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/task-skill-requirements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskSkillRequirement> updateTaskSkillRequirement(@RequestBody TaskSkillRequirement taskSkillRequirement) throws URISyntaxException {
        log.debug("REST request to update TaskSkillRequirement : {}", taskSkillRequirement);
        if (taskSkillRequirement.getId() == null) {
            return createTaskSkillRequirement(taskSkillRequirement);
        }
        TaskSkillRequirement result = taskSkillRequirementRepository.save(taskSkillRequirement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("taskSkillRequirement", taskSkillRequirement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /task-skill-requirements : get all the taskSkillRequirements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of taskSkillRequirements in body
     */
    @RequestMapping(value = "/task-skill-requirements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TaskSkillRequirement> getAllTaskSkillRequirements() {
        log.debug("REST request to get all TaskSkillRequirements");
        List<TaskSkillRequirement> taskSkillRequirements = taskSkillRequirementRepository.findAll();
        return taskSkillRequirements;
    }

    /**
     * GET  /task-skill-requirements/:id : get the "id" taskSkillRequirement.
     *
     * @param id the id of the taskSkillRequirement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskSkillRequirement, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/task-skill-requirements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskSkillRequirement> getTaskSkillRequirement(@PathVariable Long id) {
        log.debug("REST request to get TaskSkillRequirement : {}", id);
        TaskSkillRequirement taskSkillRequirement = taskSkillRequirementRepository.findOne(id);
        return Optional.ofNullable(taskSkillRequirement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /task-skill-requirements/:id : delete the "id" taskSkillRequirement.
     *
     * @param id the id of the taskSkillRequirement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/task-skill-requirements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTaskSkillRequirement(@PathVariable Long id) {
        log.debug("REST request to delete TaskSkillRequirement : {}", id);
        taskSkillRequirementRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taskSkillRequirement", id.toString())).build();
    }

}
