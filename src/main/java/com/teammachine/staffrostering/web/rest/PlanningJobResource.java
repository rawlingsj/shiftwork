package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.JobStatusUpdate;
import com.teammachine.staffrostering.domain.PlanningJob;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.domain.enumeration.JobStatus;
import com.teammachine.staffrostering.service.PlanningJobService;
import com.teammachine.staffrostering.web.rest.dto.PlanningJobWithResultDTO;
import com.teammachine.staffrostering.web.rest.errors.CustomParameterizedException;
import com.teammachine.staffrostering.web.rest.errors.ErrorConstants;
import com.teammachine.staffrostering.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//import com.teammachine.staffrostering.GreetingController;

@RestController
@RequestMapping({"/api", "/api_basic"})
public class PlanningJobResource {

    private final Logger log = LoggerFactory.getLogger(PlanningJobResource.class);

    @Inject
    private PlanningJobService planningJobService;

    @Autowired
    private SimpMessagingTemplate template;

    @RequestMapping(value = "/planning-jobs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlanningJob> createPlanningJob(@RequestBody PlanningJob planningJob) throws URISyntaxException {
        log.debug("REST request to save PlanningJob : {}", planningJob);
        if (planningJob.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("planningJob", "idexists", "A new planningJob cannot already have an ID")).body(null);
        }
        PlanningJob result = planningJobService.runPlanningJob(planningJob.getParameterization())
            .orElseThrow(() -> new CustomParameterizedException(ErrorConstants.ERR_UNABLE_TO_RUN_PLANNING_JOB));
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
        return planningJobService.findAll();
    }

    @RequestMapping(value = "/planning-jobs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> syncPlanningJobStatuses(@RequestBody(required = false) Map<String, Object> plannerServiceJob) {
        log.debug("REST request to sync PlanningJobs' statuses");
        if (plannerServiceJob != null) {
            syncOneJob(plannerServiceJob);
        } else {
            planningJobService.syncAllPlanningJobsStatuses();
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/planning-jobs-status",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> syncPlanningJobProgressStatuses(@RequestBody(required = false) Map<String, Object> plannerServiceJob) {
        /*log.debug("REST request to sync PlanningJobs' progress statuses");

        String jobId = (String) plannerServiceJob.get("jobId");
        JobStatus jobStatus = JobStatus.valueOf((String) plannerServiceJob.get("status"));
        Integer hardConstraintMatches = (Integer) plannerServiceJob.get("hardConstraintMatches");
        Integer softConstraintMatches = (Integer) plannerServiceJob.get("softConstraintMatches");
        Integer timeMillisSpent = (Integer) plannerServiceJob.get("timeMillisSpent");

        try {
            JobStatusUpdate jobStatusUpdate =  new JobStatusUpdate(jobId, jobStatus.getDescription(), hardConstraintMatches, softConstraintMatches, timeMillisSpent);
            template.convertAndSend("/topic/greetings", jobStatusUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        syncOneJob(plannerServiceJob);
        return ResponseEntity.ok().build();
    }

    private void syncOneJob(Map<String, Object> plannerServiceJob) {
        String jobId = (String) plannerServiceJob.get("jobId");
        JobStatus jobStatus = JobStatus.valueOf((String) plannerServiceJob.get("status"));
        Integer hardConstraintMatches = (Integer) plannerServiceJob.get("hardConstraintMatches");
        Integer softConstraintMatches = (Integer) plannerServiceJob.get("softConstraintMatches");
        Integer timeMillisSpent = (Integer) plannerServiceJob.get("timeMillisSpent");
        if (jobId != null && jobStatus != null) {
            JobStatusUpdate jobStatusUpdate =  new JobStatusUpdate(jobId, jobStatus.getDescription(), hardConstraintMatches, softConstraintMatches, timeMillisSpent);
            template.convertAndSend("/topic/greetings", jobStatusUpdate);
            planningJobService.updatePlanningJobStatus(jobId, jobStatus, hardConstraintMatches, softConstraintMatches, timeMillisSpent);
        }
    }

    @RequestMapping(value = "/planning-jobs/{id}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlanningJob> syncPlanningJobStatus(@PathVariable Long id) {
        log.debug("REST request to sync PlanningJob status: {}", id);
        Optional<PlanningJob> planningJob = planningJobService.findById(id);
        if (planningJob.isPresent()) {
            PlanningJob job = planningJob.get();
            planningJobService.syncPlanningJobStatus(job);
            return ResponseEntity.ok(job);
        } else
            return
                new ResponseEntity<>(HeaderUtil.createFailureAlert(PlanningJob.class.getName(),
                    ErrorConstants.ERR_NO_SUCH_PLANNING_JOB, id.toString()), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/planning-jobs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlanningJobWithResultDTO> getPlanningJob(@PathVariable Long id) {
        log.debug("REST request to get PlanningJob : {}", id);
        Optional<PlanningJob> planningJob = planningJobService.findById(id);
        if (planningJob.isPresent()) {
            PlanningJob job = planningJob.get();
            PlanningJobWithResultDTO jobDTO = new PlanningJobWithResultDTO();
            jobDTO.setId(job.getId());
            jobDTO.setJobId(job.getJobId());
            jobDTO.setStatus(job.getStatus());
            StaffRosterParametrization parameterization = job.getParameterization();
            jobDTO.setParameterization(parameterization);
            planningJobService.getPlanningJobResult(job).ifPresent(result -> {
                jobDTO.setShiftAssignments(result.getShiftAssignments());
                parameterization.setHardConstraintMatches(result.getHardConstraintMatches());
                parameterization.setSoftConstraintMatches(result.getSoftConstraintMatches());
            });
            return ResponseEntity.ok(jobDTO);
        } else
            return
                new ResponseEntity<>(HeaderUtil.createFailureAlert(PlanningJob.class.getName(),
                    ErrorConstants.ERR_NO_SUCH_PLANNING_JOB, id.toString()), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/planning-jobs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlanningJob(@PathVariable Long id) {
        log.debug("REST request to delete PlanningJob : {}", id);
        Optional<PlanningJob> planningJob = planningJobService.findById(id);
        if (planningJob.isPresent()) {
            planningJobService.terminateAndDeleteJob(planningJob.get());
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("planningJob", id.toString())).build();
        } else
            return new ResponseEntity<>(HeaderUtil.createFailureAlert(PlanningJob.class.getName(),
                ErrorConstants.ERR_NO_SUCH_PLANNING_JOB, id.toString()), HttpStatus.NOT_FOUND);
    }
}
