package com.crus.user_microservice.service;

import com.crus.user_microservice.model.User;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserDiscoveryService {

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private RestTemplate restTemplate;

    public String userServiceUrl() {
        InstanceInfo instance =
                eurekaClient.getNextServerFromEureka(
                        "user-microservice", false);

        if (instance == null) {
            throw new IllegalStateException("user-microservice not found in Eureka registry");
        }

        InstanceInfo.InstanceStatus status = eurekaClient.getInstanceRemoteStatus();

        if (status != InstanceInfo.InstanceStatus.UP) {
            throw new IllegalStateException("user-microservice is not UP");
        }
        return instance.getHomePageUrl();
    }

    public ResponseEntity<User> createRemoteUser(User user) {
        String serviceUrl = userServiceUrl();
        String createUserUrl = serviceUrl + "/user";
        System.out.println("Create Remote User");
        return restTemplate.postForEntity(createUserUrl, user, User.class);
    }
}
