package com.student.management.repository;

import com.student.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Crucial for Spring Security to find the user trying to log in
    Optional<User> findByUsername(String username);
}