package com.rushi.jobportal.repository;

import com.rushi.jobportal.model.Job;
import com.rushi.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByOrderByCreatedAtDesc();
    List<Job> findByRecruiterOrderByCreatedAtDesc(User recruiter);
}
