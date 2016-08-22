package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.PlanningJob;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.domain.enumeration.JobStatus;
import com.teammachine.staffrostering.planner.PlannerService;
import com.teammachine.staffrostering.planner.PlannerServiceJob;
import com.teammachine.staffrostering.repository.PlanningJobRepository;
import com.teammachine.staffrostering.web.rest.dto.PlanningJobWithResultDTO;
import com.teammachine.staffrostering.web.rest.errors.CustomParameterizedException;
import com.teammachine.staffrostering.web.rest.errors.ErrorConstants;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PlanningJobResource {

    private final Logger log = LoggerFactory.getLogger(PlanningJobResource.class);

    @Inject
    private PlannerService plannerService;
    @Inject
    private PlanningJobRepository planningJobRepository;

    @RequestMapping(value = "/planning-jobs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlanningJob> createPlanningJob(@RequestBody PlanningJob planningJob) throws URISyntaxException {
        log.debug("REST request to save PlanningJob : {}", planningJob);
        if (planningJob.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("planningJob", "idexists", "A new planningJob cannot already have an ID")).body(null);
        }
        PlannerServiceJob job = plannerService.runPlanningJob(planningJob.getParameterization())
            .orElseThrow(() -> new CustomParameterizedException(ErrorConstants.ERR_UNABLE_TO_RUN_PLANNING_JOB));
        planningJob.setJobId(job.getJobId());
        planningJob.setStatus(job.getStatus());
        PlanningJob result = planningJobRepository.save(planningJob);
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
        return planningJobRepository.findAll();
    }

    @RequestMapping(value = "/planning-jobs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> syncAllPlanningJobStatuses() {
        log.debug("REST request to sync PlanningJobs' statuses");
        Map<String, PlannerServiceJob> allPlanningJobs = plannerService.getAllPlanningJobs().stream()
            .collect(Collectors.toMap(PlannerServiceJob::getJobId, Function.identity()));
        planningJobRepository.findAll().stream()
            .forEach(persistedJob -> {
                PlannerServiceJob job = allPlanningJobs.get(persistedJob.getJobId());
                if (job == null) {// removed in planner engine
                    persistedJob.setStatus(null);
                    planningJobRepository.save(persistedJob);
                } else if (persistedJob.getStatus() != job.getStatus()) {// status has been updated on server
                    persistedJob.setStatus(job.getStatus());
                    planningJobRepository.save(persistedJob);
                }
            });
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/planning-jobs/{id}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlanningJob> syncPlanningJobStatus(@PathVariable Long id) {
        log.debug("REST request to sync PlanningJob status: {}", id);
        PlanningJob planningJob = planningJobRepository.findOne(id);
        if (planningJob == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        JobStatus status = plannerService.getPlanningJob(planningJob.getJobId()).map(PlannerServiceJob::getStatus).orElse(null);
        if (status != planningJob.getStatus()) {
            planningJob.setStatus(status);
            planningJobRepository.save(planningJob);
        }
        return ResponseEntity.ok(planningJob);
    }

    @RequestMapping(value = "/planning-jobs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlanningJobWithResultDTO> getPlanningJob(@PathVariable Long id) {
        log.debug("REST request to get PlanningJob : {}", id);
        PlanningJob planningJob = planningJobRepository.findOne(id);
        if (planningJob == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        PlanningJobWithResultDTO jobDTO = new PlanningJobWithResultDTO();
        jobDTO.setId(planningJob.getId());
        jobDTO.setJobId(planningJob.getJobId());
        jobDTO.setStatus(planningJob.getStatus());
        StaffRosterParametrization parameterization = planningJob.getParameterization();
        jobDTO.setParameterization(parameterization);
        plannerService.getPlanningJob(planningJob.getJobId()).ifPresent(job -> {
            jobDTO.setShiftAssignments(job.getResult().getShiftAssignments());
            parameterization.setHardConstraintMatches(job.getResult().getHardConstraintMatches());
            parameterization.setSoftConstraintMatches(job.getResult().getSoftConstraintMatches());
        });
        return ResponseEntity.ok(jobDTO);
    }

    @RequestMapping(value = "/planning-jobs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlanningJob(@PathVariable Long id) {
        log.debug("REST request to delete PlanningJob : {}", id);
        PlanningJob planningJob = planningJobRepository.findOne(id);
        if (planningJob == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        plannerService.terminateAndDeleteJob(planningJob.getJobId());
        planningJobRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("planningJob", id.toString())).build();
    }
}
