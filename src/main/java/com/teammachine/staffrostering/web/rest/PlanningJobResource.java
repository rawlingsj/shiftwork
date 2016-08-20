package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.PlanningJob;
import com.teammachine.staffrostering.domain.enumeration.JobStatus;
import com.teammachine.staffrostering.planner.PlannerService;
import com.teammachine.staffrostering.repository.PlanningJobRepository;
import com.teammachine.staffrostering.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PlanningJobResource {

    private final Logger log = LoggerFactory.getLogger(PlanningJobResource.class);

    @Inject
    private PlanningJobRepository planningJobRepository;
    @Inject
    private PlannerService plannerService;

    @RequestMapping(value = "/planning-jobs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlanningJob> createPlanningJob(@RequestBody PlanningJob planningJob) throws URISyntaxException {
        log.debug("REST request to save PlanningJob : {}", planningJob);
        if (planningJob.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("planningJob", "idexists", "A new planningJob cannot already have an ID")).body(null);
        }
        PlanningJob job = plannerService.runPlanningJob(planningJob.getParameterization());
        job.setParameterization(planningJob.getParameterization());
        PlanningJob result = planningJobRepository.save(job);
        return ResponseEntity.created(new URI("/api/planning-jobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("planningJob", result.getId().toString()))
            .body(result);
    }

    @RequestMapping(value = "/planning-jobs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PlanningJob> getAllPlanningJobs() {
        log.debug("REST request to get all PlanningJobs");
        List<PlanningJob> planningJobs = planningJobRepository.findAll();
        return planningJobs;
    }

    @RequestMapping(value = "/planning-jobs/{id}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlanningJob> syncPlanningJobStatus(@PathVariable Long id) {
        log.debug("REST request to sync PlanningJob status: {}", id);
        PlanningJob planningJob = planningJobRepository.findOne(id);
        JobStatus status = plannerService.getPlanningJobStatus(planningJob);
        if (status != planningJob.getStatus()) {
            planningJob.setStatus(status);
            planningJobRepository.save(planningJob);
        }
        return Optional.ofNullable(planningJob)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/planning-jobs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlanningJob> getPlanningJob(@PathVariable Long id) {
        log.debug("REST request to get PlanningJob : {}", id);
        PlanningJob planningJob = planningJobRepository.findOne(id);
        return Optional.ofNullable(planningJob)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/planning-jobs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlanningJob(@PathVariable Long id) {
        log.debug("REST request to delete PlanningJob : {}", id);
        PlanningJob planningJob = planningJobRepository.findOne(id);
        plannerService.terminateAndDeleteJob(planningJob);
        planningJobRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("planningJob", id.toString())).build();
    }
}
