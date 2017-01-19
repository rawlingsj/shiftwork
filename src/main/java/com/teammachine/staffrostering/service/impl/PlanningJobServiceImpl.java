package com.teammachine.staffrostering.service.impl;

import com.teammachine.staffrostering.domain.PlanningJob;
import com.teammachine.staffrostering.domain.StaffRosterParametrization;
import com.teammachine.staffrostering.domain.enumeration.JobStatus;
import com.teammachine.staffrostering.planner.PlannerEngine;
import com.teammachine.staffrostering.planner.PlannerEngineJob;
import com.teammachine.staffrostering.planner.PlannerEngineJobResult;
import com.teammachine.staffrostering.repository.PlanningJobRepository;
import com.teammachine.staffrostering.service.PlanningJobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@SuppressWarnings("unused")
public class PlanningJobServiceImpl implements PlanningJobService {

    @Inject
    private PlanningJobRepository planningJobRepository;
    @Inject
    private PlannerEngine plannerEngine;

    @Override
    public List<PlanningJob> findAll() {
        return planningJobRepository.findAll();
    }

    @Override
    public Optional<PlanningJob> findById(long id) {
        return Optional.ofNullable(planningJobRepository.findOne(id));
    }

    @Override
    public Optional<PlanningJob> runPlanningJob(StaffRosterParametrization parametrization) {
        return plannerEngine.runPlanningJob(parametrization)
            .map(plannerEngineJob -> {
                PlanningJob planningJob = new PlanningJob();
                planningJob.setJobId(plannerEngineJob.getJobId());
                planningJob.setStatus(plannerEngineJob.getStatus());
                planningJob.setParameterization(parametrization);
                return planningJobRepository.save(planningJob);
            });
    }

    @Override
    public void syncAllPlanningJobsStatuses() {
        Map<String, PlannerEngineJob> allPlanningJobs = plannerEngine.getAllPlanningJobs().stream()
            .collect(Collectors.toMap(PlannerEngineJob::getJobId, Function.identity()));
        planningJobRepository.findAll().stream()
            .forEach(persistedJob -> {
                PlannerEngineJob job = allPlanningJobs.get(persistedJob.getJobId());
                if (job == null) {// removed in planner engine
                    persistedJob.setStatus(null);
                    planningJobRepository.save(persistedJob);
                } else if (persistedJob.getStatus() != job.getStatus()) {// status has been updated on server
                    persistedJob.setStatus(job.getStatus());
                    planningJobRepository.save(persistedJob);
                }
            });
    }

    @Override
    public PlanningJob syncPlanningJobStatus(PlanningJob planningJob) {
        return plannerEngine.getPlanningJob(planningJob.getJobId())
            .map(PlannerEngineJob::getStatus)
            .filter(jobStatus -> jobStatus != planningJob.getStatus())
            .map(jobStatus -> {
                planningJob.setStatus(jobStatus);
                return planningJobRepository.save(planningJob);
            }).orElse(planningJob);
    }

    @Override
    public void updatePlanningJobStatus(String jobId, JobStatus newStatus,
                                        Integer hardConstraintMatches, Integer softConstraintMatches) {
        PlanningJob planningJob = planningJobRepository.findByJobId(jobId);
        if (planningJob != null && planningJob.getStatus() != newStatus) {
            planningJob.setStatus(newStatus);
            planningJob.setHardConstraintMatches(hardConstraintMatches);
            planningJob.setSoftConstraintMatches(softConstraintMatches);
            planningJobRepository.save(planningJob);
        }
    }

    @Override
    public void terminateAndDeleteJob(PlanningJob planningJob) {
        plannerEngine.terminateAndDeleteJob(planningJob.getJobId());
        planningJobRepository.delete(planningJob);
    }

    @Override
    public Optional<PlannerEngineJobResult> getPlanningJobResult(PlanningJob planningJob) {
        return plannerEngine.getPlanningJob(planningJob.getJobId()).map(PlannerEngineJob::getResult);
    }
}
