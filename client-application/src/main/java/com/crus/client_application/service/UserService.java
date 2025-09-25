package com.crus.client_application.service;

import com.crus.client_application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ResponseEntity<User> response = restTemplate.postForEntity
                ("http://user-microservice/user", user, User.class);
        return checkStatus(response);
    }

    public User getUser(String username) {
        ResponseEntity<User> response = restTemplate.getForEntity
                ("http://user-microservice/user/username/" + username, User.class);
        return checkStatus(response);
    }

    private User checkStatus(ResponseEntity<User> response) {
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            return null;
        }
    }
}
