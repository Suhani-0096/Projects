package com.rushi.jobportal.repository;

import com.rushi.jobportal.model.JobApplication;
import com.rushi.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByJobIdAndApplicantId(Long jobId, Long applicantId);
    List<JobApplication> findByApplicantOrderByAppliedAtDesc(User applicant);
    List<JobApplication> findByJobRecruiterOrderByAppliedAtDesc(User recruiter);
}
