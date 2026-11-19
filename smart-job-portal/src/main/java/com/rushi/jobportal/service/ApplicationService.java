package com.rushi.jobportal.service;

import com.rushi.jobportal.model.*;
import com.rushi.jobportal.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {
    private final JobApplicationRepository repository;
    private final JobService jobService;

    public ApplicationService(JobApplicationRepository repository, JobService jobService) {
        this.repository = repository;
        this.jobService = jobService;
    }

    public JobApplication apply(Long jobId, User applicant) {
        if (applicant.getRole() != Role.JOB_SEEKER) {
            throw new IllegalArgumentException("Only job seekers can apply.");
        }
        Job job = jobService.get(jobId);
        if (repository.existsByJobIdAndApplicantId(jobId, applicant.getId())) {
            throw new IllegalArgumentException("You have already applied for this job.");
        }

        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setApplicant(applicant);
        application.setAppliedAt(LocalDateTime.now());
        application.setStatus(ApplicationStatus.APPLIED);
        return repository.save(application);
    }

    public List<JobApplication> myApplications(User applicant) {
        return repository.findByApplicantOrderByAppliedAtDesc(applicant);
    }

    public List<JobApplication> recruiterApplications(User recruiter) {
        return repository.findByJobRecruiterOrderByAppliedAtDesc(recruiter);
    }

    public JobApplication updateStatus(Long id, ApplicationStatus status, User recruiter) {
        JobApplication app = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found."));
        if (!app.getJob().getRecruiter().getId().equals(recruiter.getId())) {
            throw new IllegalArgumentException("You can update only applications for your jobs.");
        }
        app.setStatus(status);
        return repository.save(app);
    }
}
