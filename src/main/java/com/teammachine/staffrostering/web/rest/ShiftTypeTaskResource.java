package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.ShiftTypeTask;
import com.teammachine.staffrostering.repository.ShiftTypeTaskRepository;
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
 * REST controller for managing ShiftTypeTask.
 */
@RestController
@RequestMapping("/api")
public class ShiftTypeTaskResource {

    private final Logger log = LoggerFactory.getLogger(ShiftTypeTaskResource.class);
        
    @Inject
    private ShiftTypeTaskRepository shiftTypeTaskRepository;
    
    /**
     * POST  /shift-type-tasks : Create a new shiftTypeTask.
     *
     * @param shiftTypeTask the shiftTypeTask to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shiftTypeTask, or with status 400 (Bad Request) if the shiftTypeTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shift-type-tasks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShiftTypeTask> createShiftTypeTask(@RequestBody ShiftTypeTask shiftTypeTask) throws URISyntaxException {
        log.debug("REST request to save ShiftTypeTask : {}", shiftTypeTask);
        if (shiftTypeTask.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("shiftTypeTask", "idexists", "A new shiftTypeTask cannot already have an ID")).body(null);
        }
        ShiftTypeTask result = shiftTypeTaskRepository.save(shiftTypeTask);
        return ResponseEntity.created(new URI("/api/shift-type-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("shiftTypeTask", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shift-type-tasks : Updates an existing shiftTypeTask.
     *
     * @param shiftTypeTask the shiftTypeTask to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shiftTypeTask,
     * or with status 400 (Bad Request) if the shiftTypeTask is not valid,
     * or with status 500 (Internal Server Error) if the shiftTypeTask couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shift-type-tasks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShiftTypeTask> updateShiftTypeTask(@RequestBody ShiftTypeTask shiftTypeTask) throws URISyntaxException {
        log.debug("REST request to update ShiftTypeTask : {}", shiftTypeTask);
        if (shiftTypeTask.getId() == null) {
            return createShiftTypeTask(shiftTypeTask);
        }
        ShiftTypeTask result = shiftTypeTaskRepository.save(shiftTypeTask);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shiftTypeTask", shiftTypeTask.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shift-type-tasks : get all the shiftTypeTasks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of shiftTypeTasks in body
     */
    @RequestMapping(value = "/shift-type-tasks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ShiftTypeTask> getAllShiftTypeTasks() {
        log.debug("REST request to get all ShiftTypeTasks");
        List<ShiftTypeTask> shiftTypeTasks = shiftTypeTaskRepository.findAll();
        return shiftTypeTasks;
    }

    /**
     * GET  /shift-type-tasks/:id : get the "id" shiftTypeTask.
     *
     * @param id the id of the shiftTypeTask to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shiftTypeTask, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/shift-type-tasks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShiftTypeTask> getShiftTypeTask(@PathVariable Long id) {
        log.debug("REST request to get ShiftTypeTask : {}", id);
        ShiftTypeTask shiftTypeTask = shiftTypeTaskRepository.findOne(id);
        return Optional.ofNullable(shiftTypeTask)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shift-type-tasks/:id : delete the "id" shiftTypeTask.
     *
     * @param id the id of the shiftTypeTask to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/shift-type-tasks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShiftTypeTask(@PathVariable Long id) {
        log.debug("REST request to delete ShiftTypeTask : {}", id);
        shiftTypeTaskRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shiftTypeTask", id.toString())).build();
    }

}
