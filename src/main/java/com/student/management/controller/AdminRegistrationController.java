package com.student.management.controller;

import com.student.management.entity.User;
import com.student.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
public class AdminRegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Secret code to prevent random sign-ups
    private final String SECRET_INVITE_CODE = "EDUTRACK2026";

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register_admin"; // Points to templates/register_admin.html
    }

    @PostMapping("/saveAdmin")
    public String saveAdmin(@ModelAttribute("user") User user, 
                            @RequestParam("inviteCode") String inviteCode,
                            RedirectAttributes ra) {
        
        // 1. Verify the Secret Code
        if (!SECRET_INVITE_CODE.equals(inviteCode)) {
            ra.addFlashAttribute("error", "Invalid Invitation Code!");
            return "redirect:/register";
        }

        // 2. Prevent Duplicate Usernames
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            ra.addFlashAttribute("error", "This Email/Username is already registered!");
            return "redirect:/register";
        }

        // 3. Encrypt Password & Assign Admin Role
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of("ROLE_ADMIN"));
        user.setEnabled(true);
        userRepository.save(user);

        ra.addFlashAttribute("success", "Admin registered successfully! You can now login.");
        return "redirect:/login";
    }
}