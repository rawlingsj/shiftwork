package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.TaskTpye;
import com.teammachine.staffrostering.repository.TaskTpyeRepository;
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
 * REST controller for managing TaskTpye.
 */
@RestController
@RequestMapping("/api")
public class TaskTpyeResource {

    private final Logger log = LoggerFactory.getLogger(TaskTpyeResource.class);
        
    @Inject
    private TaskTpyeRepository taskTpyeRepository;
    
    /**
     * POST  /task-tpyes : Create a new taskTpye.
     *
     * @param taskTpye the taskTpye to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskTpye, or with status 400 (Bad Request) if the taskTpye has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/task-tpyes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskTpye> createTaskTpye(@RequestBody TaskTpye taskTpye) throws URISyntaxException {
        log.debug("REST request to save TaskTpye : {}", taskTpye);
        if (taskTpye.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("taskTpye", "idexists", "A new taskTpye cannot already have an ID")).body(null);
        }
        TaskTpye result = taskTpyeRepository.save(taskTpye);
        return ResponseEntity.created(new URI("/api/task-tpyes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("taskTpye", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /task-tpyes : Updates an existing taskTpye.
     *
     * @param taskTpye the taskTpye to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskTpye,
     * or with status 400 (Bad Request) if the taskTpye is not valid,
     * or with status 500 (Internal Server Error) if the taskTpye couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/task-tpyes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskTpye> updateTaskTpye(@RequestBody TaskTpye taskTpye) throws URISyntaxException {
        log.debug("REST request to update TaskTpye : {}", taskTpye);
        if (taskTpye.getId() == null) {
            return createTaskTpye(taskTpye);
        }
        TaskTpye result = taskTpyeRepository.save(taskTpye);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("taskTpye", taskTpye.getId().toString()))
            .body(result);
    }

    /**
     * GET  /task-tpyes : get all the taskTpyes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of taskTpyes in body
     */
    @RequestMapping(value = "/task-tpyes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TaskTpye> getAllTaskTpyes() {
        log.debug("REST request to get all TaskTpyes");
        List<TaskTpye> taskTpyes = taskTpyeRepository.findAll();
        return taskTpyes;
    }

    /**
     * GET  /task-tpyes/:id : get the "id" taskTpye.
     *
     * @param id the id of the taskTpye to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskTpye, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/task-tpyes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskTpye> getTaskTpye(@PathVariable Long id) {
        log.debug("REST request to get TaskTpye : {}", id);
        TaskTpye taskTpye = taskTpyeRepository.findOne(id);
        return Optional.ofNullable(taskTpye)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /task-tpyes/:id : delete the "id" taskTpye.
     *
     * @param id the id of the taskTpye to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/task-tpyes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTaskTpye(@PathVariable Long id) {
        log.debug("REST request to delete TaskTpye : {}", id);
        taskTpyeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taskTpye", id.toString())).build();
    }

}
