CREATE DATABASE IF NOT EXISTS college_erp;
USE college_erp;

CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    roll_no VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL,
    course VARCHAR(80) NOT NULL,
    semester INT NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS attendance (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    subject VARCHAR(100) NOT NULL,
    total_classes INT NOT NULL,
    attended_classes INT NOT NULL,
    UNIQUE(student_id, subject),
    FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS examinations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    subject VARCHAR(100) NOT NULL,
    marks DECIMAL(5,2) NOT NULL,
    max_marks DECIMAL(5,2) NOT NULL DEFAULT 100,
    exam_type VARCHAR(50) NOT NULL,
    UNIQUE(student_id, subject, exam_type),
    FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE
);
