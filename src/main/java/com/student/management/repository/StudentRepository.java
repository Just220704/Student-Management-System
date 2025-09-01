package com.student.management.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.student.management.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    long countByCourse(String course); 

    Student findByEmail(String email);

    // NEW: Filter students by the admin who created them
    List<Student> findByAdminEmail(String adminEmail);
    
    // NEW: Count students for a specific admin (useful for dashboard cards)
    long countByAdminEmail(String adminEmail);

    // ADDED: Critical for N-Admin seat isolation
    // This allows Admin 1 to have 60 students in BCA while Admin 2 has 0 in their BCA
    long countByCourseAndAdminEmail(String course, String adminEmail);

    // ADDED: Critical for N-Admin Registration Number isolation
    // This allows Admin 2 to use a Reg No even if Admin 1 already used it
    boolean existsByRegNoAndAdminEmail(String regNo, String adminEmail);

    // ADDED: Required for the StudentService bridge method
    // This allows the Service to return the actual student record for validation
    Student findByRegNoAndAdminEmail(String regNo, String adminEmail);
}