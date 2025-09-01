package com.student.management.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.student.management.entity.Student;
import com.student.management.entity.Course;
import com.student.management.service.StudentService;
import com.student.management.service.CourseService;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- ADMIN ROUTES ---

    @GetMapping("/admin/home")
    public String viewAdminHome(Model model, Principal principal) {
        String adminEmail = principal.getName();
        // Updated to show count only for THIS admin
        model.addAttribute("totalCount", studentService.getTotalStudentCountByAdmin(adminEmail));
        return "admin_home"; 
    }

    @GetMapping("/admin/dashboard")
    public String viewAdminDashboard(Model model, Principal principal) {
        String adminEmail = principal.getName();
        // Updated to show only THIS admin's students
        model.addAttribute("listStudents", studentService.getAllStudentsByAdmin(adminEmail)); 
        return "admin_dashboard";
    }

    @GetMapping("/admin/showNewStudentForm")
    public String showNewStudentForm(Model model, Principal principal) {
        String adminEmail = principal.getName();
        Student student = new Student();
        model.addAttribute("student", student); 
        // Updated to show only THIS admin's courses in the dropdown
        model.addAttribute("listCourses", courseService.getAllCoursesByAdmin(adminEmail)); 
        return "add_student";
    }

    @PostMapping("/admin/saveStudent")
    public String saveStudent(@ModelAttribute("student") Student student, Principal principal, RedirectAttributes ra) {
        String adminEmail = principal.getName();

        try {
            // Tag the student with the logged-in admin's email immediately
            student.setAdminEmail(adminEmail);

            // 1. Duplicate RegNo check for N-Admins
            // Prevents 500 errors by checking if THIS admin already used the RegNo
            if (student.getId() == null && studentService.getStudentByRegNoAndAdmin(student.getRegNo(), adminEmail) != null) {
                ra.addFlashAttribute("error", "Failed: You already have a student with Registration Number '" + student.getRegNo() + "'!");
                return "redirect:/admin/showNewStudentForm";
            }

            // 2. Capacity check - now admin-specific
            if (studentService.isCourseFull(student.getCourse(), adminEmail)) {
                ra.addFlashAttribute("error", "Registration Failed: Your '" + student.getCourse() + "' course is at full capacity!");
                return "redirect:/admin/showNewStudentForm";
            }

            // 3. Set security credentials if it's a new student
            if (student.getId() == null && student.getRegNo() != null) {
                student.setPassword(passwordEncoder.encode(student.getRegNo()));
                student.setRole("ROLE_USER");
            }
            
            studentService.saveStudent(student); 
            ra.addFlashAttribute("success", "Student saved successfully!");
            return "redirect:/admin/dashboard"; 

        } catch (Exception e) {
            // This prevents the 500 Whitelabel Error Page
            System.out.println("STUDENT SAVE ERROR: " + e.getMessage());
            ra.addFlashAttribute("error", "Database Error: " + e.getMessage());
            return "redirect:/admin/showNewStudentForm";
        }
    }

    @GetMapping("/admin/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        String adminEmail = principal.getName();
        Student student = studentService.getStudentById(id);
        
        // Security check: Ensure admin owns this student before showing update form
        if (student == null || !student.getAdminEmail().equals(adminEmail)) {
            return "redirect:/admin/dashboard?error=unauthorized";
        }

        model.addAttribute("student", student);
        model.addAttribute("listCourses", courseService.getAllCoursesByAdmin(adminEmail));
        return "update_student"; 
    }

    @GetMapping("/admin/deleteStudent/{id}")
    public String deleteStudent(@PathVariable(value = "id") Long id, Principal principal) {
        String adminEmail = principal.getName();
        Student student = studentService.getStudentById(id);

        // Security check: Ensure admin owns this student before deleting
        if (student != null && student.getAdminEmail().equals(adminEmail)) {
            this.studentService.deleteStudentById(id);
            return "redirect:/admin/dashboard?deleted";
        }
        return "redirect:/admin/dashboard?error=unauthorized";
    }

    // --- STUDENT ROUTES ---

    @GetMapping("/student/dashboard")
    public String viewStudentDashboard(Model model, Principal principal) {
        String email = principal.getName();
        Student student = studentService.getStudentByEmail(email); 
        
        if (student == null) {
            return "redirect:/login?error=true";
        }

        // UPDATED: Find the course belonging specifically to the student's assigned admin
        Course studentCourse = null;
        if (student.getCourse() != null && student.getAdminEmail() != null) {
            studentCourse = courseService.getAllCoursesByAdmin(student.getAdminEmail())
                    .stream()
                    .filter(c -> c.getCourseCode().equals(student.getCourse()))
                    .findFirst()
                    .orElse(new Course());
        }
        
        model.addAttribute("student", student);
        model.addAttribute("studentCourse", studentCourse); 
        return "student_dashboard"; 
    }

    @GetMapping("/student/profile/{id}")
    public String viewStudentProfile(@PathVariable(value = "id") Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "student_dashboard";
    }
}