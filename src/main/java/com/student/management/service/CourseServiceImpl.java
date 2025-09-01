package com.student.management.service;

import com.student.management.entity.Course;
import com.student.management.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // Fetches courses belonging only to the specified admin
    @Override
    public List<Course> getAllCoursesByAdmin(String adminEmail) {
        return courseRepository.findByAdminEmail(adminEmail);
    }

    // Returns the count of courses created by a specific admin
    @Override
    public long getTotalCourseCountByAdmin(String adminEmail) {
        return courseRepository.countByAdminEmail(adminEmail);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public void saveCourse(Course course) {
        // Persists the course with the adminEmail set in the Controller
        this.courseRepository.save(course);
    }

    @Override
    public Course getCourseById(Long id) {
        Optional<Course> optional = courseRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Course not found for id :: " + id);
        }
    }

    @Override
    public void deleteCourseById(Long id) {
        this.courseRepository.deleteById(id);
    }

    // UPDATED: Now checks for uniqueness specifically for the logged-in admin
    // This allows Admin 1 and Admin 2 to both have a 'BCA' course
    @Override
    public boolean isCourseCodeExists(String courseCode, String adminEmail) {
        return courseRepository.existsByCourseCodeAndAdminEmail(courseCode, adminEmail);
    }

    @Override
    public Course getCourseByName(String courseName) {
        if (courseName == null) {
            return null;
        }
        // Uses the trim() method to ensure clean matching for the repository query
        Optional<Course> course = courseRepository.findByCourseName(courseName.trim());
        return course.orElse(null); 
    }
}