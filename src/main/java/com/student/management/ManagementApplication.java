package com.student.management;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.student.management.entity.Course;
import com.student.management.repository.CourseRepository;

@SpringBootApplication 
public class ManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class, args);
    }

    @Bean
    CommandLineRunner initCourses(CourseRepository repository) {
        return args -> {
            // Check if the table is empty before adding to avoid the "Only showing two" issue
            if (repository.count() == 0) {
                saveCourse(repository, "BCA", "Bachelor of Computer Applications", "IT", "3 Years", 60);
                saveCourse(repository, "MCA", "Master of Computer Applications", "IT", "2 Years", 40);
                saveCourse(repository, "B.Sc CS", "B.Sc Computer Science", "Science", "3 Years", 40);
                saveCourse(repository, "M.Sc CS", "M.Sc Computer Science", "Science", "2 Years", 30);
                saveCourse(repository, "B.Tech", "Bachelor of Technology", "Engineering", "4 Years", 120);
                saveCourse(repository, "M.Tech", "Master of Technology", "Engineering", "2 Years", 60);

                System.out.println("--- All Predefined Courses Verified and Loaded ---");
            }
        };
    }

    // Fixed the variable name from 'repo' to 'repository' to match the call
    private void saveCourse(CourseRepository repository, String code, String name, String dept, String dur, int intake) {
        Course course = new Course();
        course.setCourseCode(code);
        course.setCourseName(name);
        course.setDepartment(dept);
        course.setDuration(dur);
        course.setIntake(intake);         
        repository.save(course); // This now matches the parameter name
    }
}