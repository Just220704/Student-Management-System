package com.student.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Professional method to send contact form details to your inbox
     */
    public void sendContactEmail(String name, String email, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        
        // This is where you will receive the notification
        mailMessage.setTo("your-personal-email@gmail.com"); 
        
        mailMessage.setSubject("New EduTrack Inquiry from: " + name);
        mailMessage.setText("You have received a new message from the EduTrack Pro contact form.\n\n" +
                            "Name: " + name + "\n" +
                            "Email: " + email + "\n\n" +
                            "Message:\n" + message);

        mailSender.send(mailMessage);
    }
}