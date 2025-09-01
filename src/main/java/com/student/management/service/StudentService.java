package com.student.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.management.entity.Student;
import com.student.management.entity.Course;
import com.student.management.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseService courseService;

    // UPDATED: Get only students belonging to the logged-in admin
    public List<Student> getAllStudentsByAdmin(String adminEmail) {
        return studentRepository.findByAdminEmail(adminEmail);
    }

    // KEEPING ORIGINAL: For cases where all students might be needed (optional)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void saveStudent(Student student) {
        this.studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    // NEW: Bridge method for the Controller to check registration number isolation
    public Student getStudentByRegNoAndAdmin(String regNo, String adminEmail) {
        // First, we must ensure StudentRepository has existsByRegNoAndAdminEmail or findByRegNoAndAdminEmail
        return studentRepository.findByRegNoAndAdminEmail(regNo, adminEmail);
    }

    public void deleteStudentById(Long id) {
        this.studentRepository.deleteById(id);
    }

    // UPDATED: Filtered by Admin to ensure N-Admin isolation
    public long getStudentCountByCourse(String courseCode, String adminEmail) {
        return studentRepository.countByCourseAndAdminEmail(courseCode, adminEmail);
    }
    
    // OVERLOADED VERSION: Keeps your original call compatible if used elsewhere
    public long getStudentCountByCourse(String courseCode) {
        return studentRepository.countByCourse(courseCode);
    }

    // NEW: Get total student count for an admin's dashboard card
    public long getTotalStudentCountByAdmin(String adminEmail) {
        return studentRepository.countByAdminEmail(adminEmail);
    }

    // UPDATED: Now checks capacity only for the specific admin's course
    public boolean isCourseFull(String courseCode, String adminEmail) {
        // Count students belonging to THIS admin for this course
        long currentEnrolled = studentRepository.countByCourseAndAdminEmail(courseCode, adminEmail);
        
        // Filter course check by adminEmail to ensure we check the right admin's limits
        List<Course> allCourses = courseService.getAllCoursesByAdmin(adminEmail);
        Course selectedCourse = allCourses.stream()
                .filter(c -> c.getCourseCode().equals(courseCode))
                .findFirst()
                .orElse(null);

        if (selectedCourse != null) {
            return currentEnrolled >= selectedCourse.getIntake();
        }
        return false;
    }
}