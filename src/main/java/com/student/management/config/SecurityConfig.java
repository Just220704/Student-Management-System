package com.student.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html", "/static/**", "/style.css", "/login/**", "/admin/login/**", "/student-login", "/register", "/saveAdmin", "/images/**", "/contact/send", "/favicon.ico").permitAll() 
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/student/**").hasAuthority("ROLE_USER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    var roles = authentication.getAuthorities();
                    // Using a more robust check for roles
                    boolean isAdmin = roles.stream()
                        .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
                    
                    if (isAdmin) {
                        response.sendRedirect("/admin/home");
                    } else {
                        response.sendRedirect("/student/dashboard");
                    }
                })
                .failureHandler((request, response, exception) -> {
                    String referer = request.getHeader("Referer");
                    if (referer != null && referer.contains("/admin/login")) {
                        response.sendRedirect("/admin/login?error=true");
                    } else {
                        response.sendRedirect("/login?error=true");
                    }
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/?logout")
                .permitAll()
            )
            .sessionManagement(session -> session
                .invalidSessionUrl("/")
                // UPDATED: Session Fixation protection is critical for multi-tenancy
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .expiredUrl("/login?expired")
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/");
                })
            );

        return http.build();
    }
}