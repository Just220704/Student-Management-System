package com.student.management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "courses", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"courseCode", "adminEmail"})
})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Change: Removed unique = true from here so the Table constraint above takes over
    @Column(nullable = false)
    private String courseCode; // e.g., BCA101

    private String courseName; // e.g., Bachelor of Computer Applications
    private String department; // e.g., IT / Management
    private String duration;   // e.g., 3 Years
    private int intake;        // e.g., 60 Seats

    // NEW FIELD: This will store the modules/subjects for the student view
    @Column(length = 1000)
    private String courseSyllabus; 

    // NEW: Field for Multi-Tenancy (Admin Ownership)
    private String adminEmail;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public int getIntake() { return intake; }
    public void setIntake(int intake) { this.intake = intake; }

    // Getters and Setters for Syllabus
    public String getCourseSyllabus() { return courseSyllabus; }
    public void setCourseSyllabus(String courseSyllabus) { this.courseSyllabus = courseSyllabus; }

    // NEW: Getter and Setter for Admin Ownership
    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }
}