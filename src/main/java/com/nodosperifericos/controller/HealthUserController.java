package com.nodosperifericos.controller;

import com.nodosperifericos.dto.request.CreateHealthUserRequest;
import com.nodosperifericos.dto.response.FindAllHealthUsersResponse;
import com.nodosperifericos.integration.hcen.HcenHealthUserClient;
import com.nodosperifericos.service.HealthUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/health-users")
@RequiredArgsConstructor
public class HealthUserController {
    
    private final HealthUserService healthUserService;
    private final HcenHealthUserClient hcenHealthUserClient;
    
    @GetMapping
    public ResponseEntity<FindAllHealthUsersResponse> getHealthUsers(
            @RequestParam(required = false, defaultValue = "0") Integer pageIndex,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String ci,
            @RequestParam(required = false) String clinicName) {
        
        Map<String, String> searchParams = new java.util.HashMap<>();
        searchParams.put("pageIndex", String.valueOf(pageIndex));
        searchParams.put("pageSize", String.valueOf(pageSize));
        if (name != null && !name.trim().isEmpty()) searchParams.put("name", name.trim());
        if (ci != null && !ci.trim().isEmpty()) searchParams.put("ci", ci.trim());
        if (clinicName != null && !clinicName.trim().isEmpty()) searchParams.put("clinicName", clinicName.trim());
        
        // HCEN returns just the array of users, not paginated metadata
        List<HcenHealthUserClient.HealthUser> users = hcenHealthUserClient.fetchHealthUsers(searchParams);
        
        // Transform to expected paginated format (matching Next.js logic)
        FindAllHealthUsersResponse response = FindAllHealthUsersResponse.builder()
                .items(users)
                .page(pageIndex)
                .size(pageSize)
                .totalItems(users.size()) // HCEN doesn't provide total count
                .totalPages(1) // HCEN doesn't provide pagination info
                .hasNext(false)
                .hasPrevious(false)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<HcenHealthUserClient.HealthUser> createHealthUser(
            @Valid @RequestBody CreateHealthUserRequest request) {
        
        HcenHealthUserClient.CreateHealthUserRequest hcenRequest = new HcenHealthUserClient.CreateHealthUserRequest();
        hcenRequest.setCi(request.getCi());
        hcenRequest.setFirstName(request.getFirstName());
        hcenRequest.setLastName(request.getLastName());
        hcenRequest.setGender(request.getGender());
        hcenRequest.setEmail(request.getEmail());
        hcenRequest.setPhone(request.getPhone());
        hcenRequest.setAddress(request.getAddress());
        hcenRequest.setDateOfBirth(request.getDateOfBirth().toString());
        hcenRequest.setClinicNames(request.getClinicNames());
        
        HcenHealthUserClient.HealthUser created = hcenHealthUserClient.createHealthUser(hcenRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}


