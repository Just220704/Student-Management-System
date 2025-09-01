package com.student.management.service;

import com.student.management.entity.Course;
import java.util.List;

public interface CourseService {
    // UPDATED: Admin Dashboard methods now supporting multi-tenancy
    List<Course> getAllCoursesByAdmin(String adminEmail);
    
    // EXISTING: Admin Dashboard methods
    List<Course> getAllCourses();
    void saveCourse(Course course);
    Course getCourseById(Long id);
    void deleteCourseById(Long id);

    // UPDATED: Now checks for existence based on the specific Admin's email
    // This allows Admin 1 to have 'BCA' and Admin 2 to also have 'BCA'
    boolean isCourseCodeExists(String courseCode, String adminEmail);

    // NEW: Used by Student Dashboard to fetch syllabus/modules
    Course getCourseByName(String courseName);

    // NEW: Helper for Admin Home dashboard count
    long getTotalCourseCountByAdmin(String adminEmail);
}