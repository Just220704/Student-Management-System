package com.student.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.student.management.service.EmailService;

@Controller
public class HomeController {

    @Autowired
    private EmailService emailService;

    // We removed @GetMapping("/") because index.html is in the static folder

    /**
     * Processes the Contact Form submission
     * It captures the name, email, and message from the UI
     */
    @PostMapping("/contact/send")
    public String handleContactForm(@RequestParam("name") String name,
                                   @RequestParam("email") String email,
                                   @RequestParam("message") String message,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Trigger the email notification to your inbox
            emailService.sendContactEmail(name, email, message);
            
            // Add a success message
            redirectAttributes.addFlashAttribute("success", "Message sent successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to send message. Please try again.");
        }
        
        // Redirecting specifically to index.html in the static folder
        return "redirect:/index.html#contact"; 
    }
}