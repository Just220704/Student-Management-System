package com.student.management.repository;

import com.student.management.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // UPDATED: Check for duplicates ONLY for the specific admin
    // This allows Admin 1 and Admin 2 to both have a 'BCA' course
    boolean existsByCourseCodeAndAdminEmail(String courseCode, String adminEmail);

    // EXISTING: Used by Admin to prevent system-wide duplicates (if still needed)
    boolean existsByCourseCode(String courseCode);

    // NEW: Used by Student Dashboard to fetch Syllabus/Modules
    Optional<Course> findByCourseName(String courseName);

    // NEW: Filter courses by the admin who created them
    List<Course> findByAdminEmail(String adminEmail);

    // NEW: Count courses for a specific admin
    long countByAdminEmail(String adminEmail);
}