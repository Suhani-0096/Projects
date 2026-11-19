package com.rushi.jobportal.service;

import com.rushi.jobportal.model.Job;
import com.rushi.jobportal.model.User;
import com.rushi.jobportal.repository.JobRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> allJobs() {
        return jobRepository.findAllByOrderByCreatedAtDesc();
    }

    public Job create(Job job, User recruiter) {
        validate(job);
        job.setId(null);
        job.setRecruiter(recruiter);
        job.setCreatedAt(LocalDateTime.now());
        return jobRepository.save(job);
    }

    public Job update(Long id, Job input, User recruiter) {
        Job job = get(id);
        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new IllegalArgumentException("You can update only your own jobs.");
        }
        validate(input);
        job.setTitle(input.getTitle());
        job.setCompany(input.getCompany());
        job.setDescription(input.getDescription());
        job.setLocation(input.getLocation());
        job.setSkills(input.getSkills());
        job.setSalary(input.getSalary());
        return jobRepository.save(job);
    }

    public void delete(Long id, User recruiter) {
        Job job = get(id);
        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new IllegalArgumentException("You can delete only your own jobs.");
        }
        jobRepository.delete(job);
    }

    public Job get(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found."));
    }

    public List<Job> recruiterJobs(User recruiter) {
        return jobRepository.findByRecruiterOrderByCreatedAtDesc(recruiter);
    }

    private void validate(Job job) {
        if (job.getTitle() == null || job.getTitle().isBlank()
                || job.getCompany() == null || job.getCompany().isBlank()
                || job.getDescription() == null || job.getDescription().isBlank()
                || job.getLocation() == null || job.getLocation().isBlank()
                || job.getSkills() == null || job.getSkills().isBlank()) {
            throw new IllegalArgumentException("Title, company, description, location and skills are required.");
        }
    }
}
