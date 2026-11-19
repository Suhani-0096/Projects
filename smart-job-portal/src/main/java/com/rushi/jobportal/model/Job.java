package com.rushi.jobportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false, length = 4000)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String skills;

    private Double salary;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    public Job() {}

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCompany() { return company; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getSkills() { return skills; }
    public Double getSalary() { return salary; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public User getRecruiter() { return recruiter; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setCompany(String company) { this.company = company; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setSkills(String skills) { this.skills = skills; }
    public void setSalary(Double salary) { this.salary = salary; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setRecruiter(User recruiter) { this.recruiter = recruiter; }
}
