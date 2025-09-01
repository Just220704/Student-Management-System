package com.student.management.controller;

import com.student.management.entity.User;
import com.student.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/security")
public class UserManagementController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // UPDATED: View ONLY the currently logged-in Admin's info
    @GetMapping("/users")
    public String viewUsers(Model model, Principal principal) {
        // Fetch only the logged-in user's profile
        String username = principal.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Wrap in a list so the existing thymeleaf each loop still works
        model.addAttribute("listUsers", List.of(currentUser));
        return "admin_users"; 
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of("ROLE_ADMIN")); 
        user.setEnabled(true);
        userRepository.save(user);
        return "redirect:/admin/security/users?success";
    }

    // UPDATED: Security check to prevent deleting other admins
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Principal principal, RedirectAttributes ra) {
        String loggedInUsername = principal.getName();
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // SECURITY CHECK: An admin can only delete their OWN account
        if (!userToDelete.getUsername().equals(loggedInUsername)) {
            ra.addFlashAttribute("error", "Unauthorized: You cannot remove other administrator accounts.");
            return "redirect:/admin/security/users";
        }

        userRepository.delete(userToDelete);
        ra.addFlashAttribute("success", "Your administrator access has been removed.");
        
        // Since they deleted themselves, redirect to logout
        return "redirect:/logout";
    }
}