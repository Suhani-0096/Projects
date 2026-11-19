# Smart Job Portal System

A complete beginner-friendly Job Portal built with:

- Java 17
- Spring Boot 3.5
- Spring Data JPA / Hibernate
- MySQL
- HTML, CSS, JavaScript
- BCrypt password hashing
- REST APIs
- Session-based authentication
- Role-based functionality

## Features

### Job Seeker
- Register/login
- Browse all jobs
- Search jobs
- Apply to jobs
- Prevent duplicate applications
- View application history and status

### Recruiter
- Register/login
- Post jobs
- View own jobs
- Delete own jobs
- View applications received
- Shortlist/reject applicants

## Database

1. Install MySQL.
2. Run `database/schema.sql`.
3. Open `src/main/resources/application.properties`.
4. Change:
   - `spring.datasource.username`
   - `spring.datasource.password`

Default example:
```properties
spring.datasource.username=root
spring.datasource.password=root
```

## Run

Requirements:
- JDK 17+
- Maven 3.9+
- MySQL 8+

From the project folder:

```bash
mvn spring-boot:run
```

Then open:

http://localhost:8080

Or build:

```bash
mvn clean package
java -jar target/smart-job-portal-1.0.0.jar
```

## Suggested demo

Create two accounts:
1. Recruiter
2. Job Seeker

Login as recruiter -> publish a Java/Spring Boot job.
Logout -> login as job seeker -> apply.
Login again as recruiter -> open dashboard -> shortlist/reject the applicant.

## Project structure

```text
smart-job-portal/
├── pom.xml
├── README.md
├── database/
│   └── schema.sql
└── src/
    └── main/
        ├── java/com/rushi/jobportal/
        │   ├── SmartJobPortalApplication.java
        │   ├── config/
        │   ├── controller/
        │   ├── model/
        │   ├── repository/
        │   └── service/
        └── resources/
            ├── application.properties
            └── static/
                ├── index.html
                ├── style.css
                └── app.js
```

## CV alignment

This implementation directly covers the project claims in the CV:
- Java + Spring Boot web application
- Job seekers and recruiters
- Authentication
- Role-based functionality
- MySQL data management
- Recruiter dashboard
- Job posting management
- Application management
- Git/GitHub-ready Maven project
