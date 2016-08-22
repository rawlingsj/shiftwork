package com.teammachine.staffrostering.planner.impl;

import com.teammachine.staffrostering.config.PlannerEngineConnectionProperties;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.planner.PlannerService;
import com.teammachine.staffrostering.planner.PlannerServiceJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@SuppressWarnings("unused")
public class PlannerServiceImpl implements PlannerService {

    private final Logger logger = LoggerFactory.getLogger(PlannerServiceImpl.class);

    @Inject
    private PlannerEngineConnectionProperties plannerEngineConnectionProperties;
    @Inject
    private PlannerServiceRestTemplateFactory restTemplateFactory;

    private String getPlanningJobRestEndPoint() {
        String url = plannerEngineConnectionProperties.getUrl();
        if (url == null) {
            throw new IllegalStateException("URL to Planner Engine is not specified");
        }
        return url + "/planning-jobs";
    }

    @Override
    public List<PlannerServiceJob> getAllPlanningJobs() {
        logger.debug("Request to get all planning jobs");
        ResponseEntity<PlannerServiceJobImpl[]> response = restTemplateFactory.getRestTemplate().getForEntity(getPlanningJobRestEndPoint(), PlannerServiceJobImpl[].class);
        logger.debug("Request to get all planning jobs executed with status: {}", response.getStatusCode().value());
        return Arrays.asList(response.getBody());
    }

    @Override
    public Optional<PlannerServiceJob> getPlanningJob(String jobId) {
        logger.debug("Request to get planning job with id: {}", jobId);
        try {
            ResponseEntity<PlannerServiceJobImpl> response = restTemplateFactory.getRestTemplate().getForEntity(getPlanningJobRestEndPoint() + "/{jobId}", PlannerServiceJobImpl.class, jobId);
            logger.debug("Request to get planning job executed with status: {}", response.getStatusCode().value());
            return Optional.of(response.getBody());
        } catch (HttpClientErrorException e) {
            logger.debug("Request to get planning job executed with status: {}", e.getStatusCode().value());
            return Optional.empty();
        }
    }

    @Override
    public Optional<PlannerServiceJob> runPlanningJob(StaffRosterParametrization staffRosterParametrization) {
        logger.debug("Request to run planning job with parametrization: {}", staffRosterParametrization);
        try {
            ResponseEntity<PlannerServiceJobImpl> response = restTemplateFactory.getRestTemplate().postForEntity(getPlanningJobRestEndPoint(), staffRosterParametrization, PlannerServiceJobImpl.class);
            logger.debug("Request to run planning job executed with status: {}", response.getStatusCode().value());
            return Optional.of(response.getBody());
        } catch (HttpClientErrorException e) {
            logger.debug("Request to run planning job executed with status: {}", e.getStatusCode().value());
            return Optional.empty();
        }
    }

    @Override
    public void terminateAndDeleteJob(String jobId) {
        logger.debug("Request to terminate and delete planning job with id: {}", jobId);
        restTemplateFactory.getRestTemplate().delete(getPlanningJobRestEndPoint() + "/{jobId}", jobId);
        logger.debug("Request to terminate and delete planning job with id: {} executed", jobId);
    }
}
