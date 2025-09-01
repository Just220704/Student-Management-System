package com.student.management.entity;

import jakarta.persistence.*;

@Entity 
@Table(name = "students", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"registration_number", "adminEmail"})
})
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    
    // REMOVED unique = true here to allow different admins to use same reg numbers
    @Column(name = "registration_number", nullable = false) 
    private String regNo;
    
    private String name;
    private String email;
    private String course;
    
    private String password;
    private String role;

    private String adminEmail;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }
}