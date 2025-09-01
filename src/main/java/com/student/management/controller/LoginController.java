package com.student.management.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

    /**
     * Serves the Student Portal login page.
     * Accessible via: /login
     * Template: src/main/resources/templates/student-login.html
     */
    @GetMapping("/login")
    public String studentLogin() {
        return "student-login"; 
    }

    /**
     * Serves the Administrator Management login page.
     * Accessible via: /admin/login
     * Template: src/main/resources/templates/login.html
     */
    @GetMapping("/admin/login")
    public String adminLogin() {
        return "login"; 
    }
}