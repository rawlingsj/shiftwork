package com.teammachine.staffrostering.planner.impl;

import com.teammachine.staffrostering.domain.PlanningJob;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.domain.enumeration.JobStatus;
import com.teammachine.staffrostering.planner.PlannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PlannerServiceImpl implements PlannerService {

    private static final String PLANNER_ENGINE_URI = "http://localhost:8082/plannerengine-0.0.1-SNAPSHOT/rest";

    private final Logger logger = LoggerFactory.getLogger(PlannerServiceImpl.class);

    @Override
    public PlanningJob runPlanningJob(StaffRosterParametrization staffRosterParametrization) {
        logger.debug("Request to run planning job with parametrization: {}", staffRosterParametrization);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PlanningJob> response = restTemplate.postForEntity(PLANNER_ENGINE_URI + "/planning-jobs", staffRosterParametrization, PlanningJob.class);
        logger.debug("Request to run planning job executed with status: {}", response.getStatusCode().getReasonPhrase());
        return response.getBody();
    }

    @Override
    public JobStatus getPlanningJobStatus(PlanningJob planningJob) {
        logger.debug("Request to get planning job status with id: {}", planningJob.getJobId());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PlanningJob> response = restTemplate.getForEntity(PLANNER_ENGINE_URI + "/planning-jobs/{jobId}", PlanningJob.class, planningJob.getJobId());
        logger.debug("Request to get planning job status executed with status: {}", response.getStatusCode().getReasonPhrase());
        return response.getBody().getStatus();
    }

    @Override
    public void terminateAndDeleteJob(PlanningJob planningJob) {
        logger.debug("Request to terminate and delete planning job with id: {}", planningJob.getJobId());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(PLANNER_ENGINE_URI + "/planning-jobs/{jobId}", planningJob.getJobId());
        logger.debug("Request to terminate and delete planning job with id: {} executed", planningJob.getJobId());
    }
}
