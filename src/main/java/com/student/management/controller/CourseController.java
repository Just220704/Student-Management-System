package com.student.management.controller;

import com.student.management.entity.Course;
import com.student.management.service.CourseService;
import com.student.management.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    // 1. View Dashboard (Filtered by Admin)
    @GetMapping("/admin/courses")
    public String viewCourseDashboard(Model model, Principal principal) {
        // Correctly pulls the unique identity for N-admin support
        String adminEmail = principal.getName(); 
        List<Course> courses = courseService.getAllCoursesByAdmin(adminEmail);
        
        Map<String, Long> seatCounts = new HashMap<>();
        for (Course c : courses) {
            // UPDATED: Now passing adminEmail to ensure counts are isolated per admin
            seatCounts.put(c.getCourseCode(), studentService.getStudentCountByCourse(c.getCourseCode(), adminEmail));
        }
        
        model.addAttribute("listCourses", courses);
        model.addAttribute("seatCounts", seatCounts);
        return "admin_courses";
    }

    // 2. Show Add Course Form
    @GetMapping("/admin/showNewCourseForm")
    public String showNewCourseForm(Model model) {
        Course course = new Course();
        model.addAttribute("course", course);
        return "add_course";
    }

    // 3. Save Course to DB
    @PostMapping("/admin/saveCourse")
    public String saveCourse(@ModelAttribute("course") Course course, Principal principal, RedirectAttributes ra) {
        String adminEmail = principal.getName();
        // Tagging ensures this course belongs ONLY to this admin
        course.setAdminEmail(adminEmail);

        try {
            // UPDATED: Now passing adminEmail to allow duplicate codes across DIFFERENT admins
            if (course.getId() == null && courseService.isCourseCodeExists(course.getCourseCode(), adminEmail)) {
                ra.addFlashAttribute("error", "Failed: You already have a course with code '" + course.getCourseCode() + "'!");
                return "redirect:/admin/showNewCourseForm";
            }
            
            courseService.saveCourse(course);
            ra.addFlashAttribute("success", "Course saved!");
        } catch (Exception e) {
            System.out.println("CRITICAL ERROR: " + e.getMessage());
            ra.addFlashAttribute("error", "Database Error: " + e.getMessage());
            return "redirect:/admin/showNewCourseForm";
        }
        return "redirect:/admin/courses";
    }

    // 4. Show Form for Update (Security Check Included)
    @GetMapping("/admin/showFormForCourseUpdate/{id}")
    public String showFormForCourseUpdate(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        String adminEmail = principal.getName();
        Course course = courseService.getCourseById(id);
        
        // Prevents Admin 2 from accessing Admin 1's courses via URL manipulation
        if (course == null || !course.getAdminEmail().equals(adminEmail)) {
            return "redirect:/admin/courses?error=unauthorized";
        }

        model.addAttribute("course", course);
        return "update_course";
    }

    // 5. Delete Course (Security Check Included)
    @GetMapping("/admin/deleteCourse/{id}")
    public String deleteCourse(@PathVariable(value = "id") Long id, Principal principal, RedirectAttributes ra) {
        String adminEmail = principal.getName();
        Course course = courseService.getCourseById(id);

        if (course == null || !course.getAdminEmail().equals(adminEmail)) {
            return "redirect:/admin/courses?error=unauthorized";
        }

        // UPDATED: Now passing adminEmail to ensure we check the right admin's students
        long enrolledCount = studentService.getStudentCountByCourse(course.getCourseCode(), adminEmail);
        
        if (enrolledCount > 0) {
            ra.addFlashAttribute("error", "Cannot archive '" + course.getCourseName() + "'. There are still " + enrolledCount + " students enrolled!");
            return "redirect:/admin/courses";
        }

        this.courseService.deleteCourseById(id);
        return "redirect:/admin/courses?deleted";
    }
}