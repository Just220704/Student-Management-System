package com.student.management.service;

import com.student.management.entity.User;
import com.student.management.entity.Student;
import com.student.management.repository.UserRepository;
import com.student.management.repository.StudentRepository; // Added this
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository; // Injected Student Repository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // 1. First, try to find an Admin in the 'users' table
        var adminUser = userRepository.findByUsername(username);
        if (adminUser.isPresent()) {
            User user = adminUser.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.isEnabled(),
                    true, true, true,
                    user.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList())
            );
        }

        // 2. If no Admin found, try to find a Student in the 'students' table
        Student student = studentRepository.findByEmail(username); // Search by Email
        if (student != null) {
            return new org.springframework.security.core.userdetails.User(
                    student.getEmail(),
                    student.getPassword(), // This will be the encoded Reg No we set up
                    true, // isEnabled
                    true, true, true,
                    Collections.singletonList(new SimpleGrantedAuthority(student.getRole()))
            );
        }

        // 3. If neither exists, throw the exception
        throw new UsernameNotFoundException("User or Student not found with: " + username);
    }
}