# EduTrack Pro - N-Admin Student Management System

A multi-tenant academic management platform built with Spring Boot, designed to support multiple independent administrators within a single system.

## 🚀 Key Features
* **N-Admin Data Isolation**: Each administrator has a completely private workspace. One admin cannot see or modify another's students, courses, or settings.
* **Resource Conflict Resolution**: Independent registration number tracking allows different admins to use identical student ID formats without database conflicts.
* **Course Capacity Management**: Per-admin intake limits prevent over-enrollment in specific course branches.
* **Personalized Dashboard**: Dynamic statistics that only count records belonging to the currently logged-in administrator.
* **Secure Authentication**: Robust role-based security using Spring Security and BCrypt password encoding.

## 🛠️ Tech Stack
* **Java 17+** & **Spring Boot 3**
* **Spring Security** (Principal-based filtering)
* **Spring Data JPA** (MySQL)
* **Thymeleaf** & **CSS3**
* **JavaMailSender** (Email notifications)

## 📋 Setup Instructions
1. Clone the repository.
2. Configure your database and Gmail App Password in `src/main/resources/application.properties`.
3. The system will automatically run `schema.sql` to set up the necessary N-Admin composite unique constraints.
4. Run the application and navigate to `http://localhost:8080`.