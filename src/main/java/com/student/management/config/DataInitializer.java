package com.student.management.config;

import com.student.management.entity.User;
import com.student.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if our test admin already exists
        if (userRepository.findByUsername("admin@edutrack.com").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin@edutrack.com");
            
            // Encrypting the password 'admin123'
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of("ROLE_ADMIN"));
            admin.setEnabled(true);
            
            userRepository.save(admin);
            System.out.println("TEST ADMIN CREATED: User: admin@edutrack.com | Pass: admin123");
        }
    }
}