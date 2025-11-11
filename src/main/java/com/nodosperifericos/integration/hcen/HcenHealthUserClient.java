package com.nodosperifericos.integration.hcen;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HcenHealthUserClient {
    
    private final HcenApiClient hcenApiClient;
    
    @Data
    public static class HealthUser {
        private String id;
        private String ci;
        private String firstName;
        private String lastName;
        private String gender;
        private String email;
        private String phone;
        private String address;
        private String dateOfBirth;
        private String createdAt;
        private String updatedAt;
        private List<String> clinicNames;
    }
    
    @Data
    public static class CreateHealthUserRequest {
        private String ci;
        private String firstName;
        private String lastName;
        private String gender;
        private String email;
        private String phone;
        private String address;
        private String dateOfBirth;
        private List<String> clinicNames;
    }
    
    @SuppressWarnings("unchecked")
    public List<HealthUser> fetchHealthUsers(Map<String, String> searchParams) {
        return (List<HealthUser>) hcenApiClient.get("health-users", List.class, searchParams);
    }
    
    public HealthUser createHealthUser(CreateHealthUserRequest request) {
        return hcenApiClient.post("health-users", request, HealthUser.class);
    }
}

