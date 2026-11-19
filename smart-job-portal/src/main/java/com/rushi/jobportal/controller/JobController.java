package com.rushi.jobportal.controller;

import com.rushi.jobportal.model.*;
import com.rushi.jobportal.repository.UserRepository;
import com.rushi.jobportal.service.ApplicationService;
import com.rushi.jobportal.service.JobService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class JobController {
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final UserRepository userRepository;

    public JobController(JobService jobService, ApplicationService applicationService,
                         UserRepository userRepository) {
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/jobs")
    public List<Map<String, Object>> jobs() {
        return jobService.allJobs().stream().map(this::jobView).toList();
    }

    @PostMapping("/jobs")
    public Map<String, Object> create(@RequestBody Job job, HttpSession session) {
        User recruiter = requireRole(session, Role.RECRUITER);
        return jobView(jobService.create(job, recruiter));
    }

    @PutMapping("/jobs/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody Job job, HttpSession session) {
        User recruiter = requireRole(session, Role.RECRUITER);
        return jobView(jobService.update(id, job, recruiter));
    }

    @DeleteMapping("/jobs/{id}")
    public Map<String, String> delete(@PathVariable Long id, HttpSession session) {
        User recruiter = requireRole(session, Role.RECRUITER);
        jobService.delete(id, recruiter);
        return Map.of("message", "Job deleted.");
    }

    @GetMapping("/recruiter/jobs")
    public List<Map<String, Object>> recruiterJobs(HttpSession session) {
        User recruiter = requireRole(session, Role.RECRUITER);
        return jobService.recruiterJobs(recruiter).stream().map(this::jobView).toList();
    }

    @PostMapping("/jobs/{jobId}/apply")
    public Map<String, Object> apply(@PathVariable Long jobId, HttpSession session) {
        User applicant = requireRole(session, Role.JOB_SEEKER);
        return applicationView(applicationService.apply(jobId, applicant));
    }

    @GetMapping("/applications/me")
    public List<Map<String, Object>> myApplications(HttpSession session) {
        User applicant = requireRole(session, Role.JOB_SEEKER);
        return applicationService.myApplications(applicant).stream().map(this::applicationView).toList();
    }

    @GetMapping("/recruiter/applications")
    public List<Map<String, Object>> recruiterApplications(HttpSession session) {
        User recruiter = requireRole(session, Role.RECRUITER);
        return applicationService.recruiterApplications(recruiter).stream().map(this::applicationView).toList();
    }

    @PutMapping("/applications/{id}/status")
    public Map<String, Object> status(@PathVariable Long id,
                                      @RequestBody Map<String, String> body,
                                      HttpSession session) {
        User recruiter = requireRole(session, Role.RECRUITER);
        ApplicationStatus status = ApplicationStatus.valueOf(body.get("status").toUpperCase());
        return applicationView(applicationService.updateStatus(id, status, recruiter));
    }

    private User currentUser(HttpSession session) {
        Object id = session.getAttribute("USER_ID");
        if (id == null) throw new IllegalArgumentException("Please login first.");
        return userRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    private User requireRole(HttpSession session, Role role) {
        User user = currentUser(session);
        if (user.getRole() != role) throw new IllegalArgumentException("Access denied for your role.");
        return user;
    }

    private Map<String, Object> jobView(Job job) {
        return Map.of(
            "id", job.getId(),
            "title", job.getTitle(),
            "company", job.getCompany(),
            "description", job.getDescription(),
            "location", job.getLocation(),
            "skills", job.getSkills(),
            "salary", job.getSalary() == null ? 0 : job.getSalary(),
            "createdAt", job.getCreatedAt(),
            "recruiterName", job.getRecruiter().getName()
        );
    }

    private Map<String, Object> applicationView(JobApplication a) {
        return Map.of(
            "id", a.getId(),
            "jobId", a.getJob().getId(),
            "jobTitle", a.getJob().getTitle(),
            "company", a.getJob().getCompany(),
            "applicantName", a.getApplicant().getName(),
            "applicantEmail", a.getApplicant().getEmail(),
            "status", a.getStatus(),
            "appliedAt", a.getAppliedAt()
        );
    }
}
