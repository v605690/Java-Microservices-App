package com.crus.user_microservice.repositories.UserRepository;

import com.crus.user_microservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
