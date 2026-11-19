# College ERP Management System

Java + JDBC + MySQL console-based ERP for students, attendance and examinations.

## Modules
- Student management: Add, View, Search, Update, Delete
- Attendance: Record/update attendance and view attendance
- Examination: Add marks, view marks, calculate percentage/result
- MySQL persistence through JDBC
- OOP classes, DAO-style database operations, validation and exception handling

## Requirements
- JDK 17+
- MySQL 8+
- Maven 3.9+

## Setup
1. Run `database/schema.sql` in MySQL.
2. Open `Connectivity.java` and set your MySQL username/password.
3. Import this as a Maven project.
4. Run `CollegeERPApplication.java`.

## Default database
Database: `college_erp`
Username: `root`
Password: `root` (change it if needed)

## Demo
Add a student, record attendance, enter examination marks, then use the menu to view the student's academic record.
