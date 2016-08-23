package com.teammachine.staffrostering.planner.impl;

import com.teammachine.staffrostering.config.PlannerEngineConnectionProperties;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.planner.PlannerEngine;
import com.teammachine.staffrostering.planner.PlannerEngineJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@SuppressWarnings("unused")
public class PlannerEngineImpl implements PlannerEngine {

    private final Logger logger = LoggerFactory.getLogger(PlannerEngineImpl.class);

    @Inject
    private PlannerEngineConnectionProperties plannerEngineConnectionProperties;
    @Inject
    private PlannerEngineRestTemplateFactory restTemplateFactory;

    private String getPlanningJobRestEndPoint() {
        String url = plannerEngineConnectionProperties.getUrl();
        if (url == null) {
            throw new IllegalStateException("URL to Planner Engine is not specified");
        }
        return url + "/planning-jobs";
    }

    @Override
    public List<PlannerEngineJob> getAllPlanningJobs() {
        try {
            logger.debug("Request to get all planning jobs");
            ResponseEntity<PlannerEngineJobImpl[]> response = restTemplateFactory.getRestTemplate().getForEntity(getPlanningJobRestEndPoint(), PlannerEngineJobImpl[].class);
            logger.debug("Request to get all planning jobs executed with status: {}", response.getStatusCode().value());
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException e) {
            logger.error("Request to get all planning jobs failed with status: {}", e.getStatusCode().value());
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<PlannerEngineJob> getPlanningJob(String jobId) {
        try {
            logger.debug("Request to get planning job with id: {}", jobId);
            ResponseEntity<PlannerEngineJobImpl> response = restTemplateFactory.getRestTemplate().getForEntity(getPlanningJobRestEndPoint() + "/{jobId}", PlannerEngineJobImpl.class, jobId);
            logger.debug("Request to get planning job executed with status: {}", response.getStatusCode().value());
            return Optional.of(response.getBody());
        } catch (HttpClientErrorException e) {
            logger.error("Request to get planning job failed with status: {}", e.getStatusCode().value());
            return Optional.empty();
        }
    }

    @Override
    public Optional<PlannerEngineJob> runPlanningJob(StaffRosterParametrization staffRosterParametrization) {
        try {
            logger.debug("Request to run planning job with parametrization: {}", staffRosterParametrization);
            ResponseEntity<PlannerEngineJobImpl> response = restTemplateFactory.getRestTemplate().postForEntity(getPlanningJobRestEndPoint(), staffRosterParametrization, PlannerEngineJobImpl.class);
            logger.debug("Request to run planning job executed with status: {}", response.getStatusCode().value());
            return Optional.of(response.getBody());
        } catch (HttpClientErrorException e) {
            logger.error("Request to run planning job failed with status: {}", e.getStatusCode().value());
            return Optional.empty();
        }
    }

    @Override
    public void terminateAndDeleteJob(String jobId) {
        try {
            logger.debug("Request to terminate and delete planning job with id: {}", jobId);
            restTemplateFactory.getRestTemplate().delete(getPlanningJobRestEndPoint() + "/{jobId}", jobId);
            logger.debug("Request to terminate and delete planning job with id: {} executed", jobId);
        } catch (HttpClientErrorException e) {
            logger.error("Request to terminate and delete planning job failed with status: {}", e.getStatusCode().value());
        }
    }
}
