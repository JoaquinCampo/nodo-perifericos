package com.nodosperifericos.controller;

import com.nodosperifericos.domain.Clinic;
import com.nodosperifericos.domain.ClinicAdmin;
import com.nodosperifericos.domain.HealthWorker;
import com.nodosperifericos.domain.User;
import com.nodosperifericos.dto.request.CreateClinicRequest;
import com.nodosperifericos.exception.ResourceNotFoundException;
import com.nodosperifericos.repository.ClinicRepository;
import com.nodosperifericos.repository.UserRepository;
import com.nodosperifericos.service.ClinicAdminService;
import com.nodosperifericos.service.ClinicService;
import com.nodosperifericos.service.HealthWorkerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clinics")
@RequiredArgsConstructor
public class ClinicController {
    
    private final ClinicService clinicService;
    private final HealthWorkerService healthWorkerService;
    private final ClinicAdminService clinicAdminService;
    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final PasswordEncoder passwordEncoder;
    
    @GetMapping
    public ResponseEntity<List<Clinic>> getAllClinics() {
        return ResponseEntity.ok(clinicService.findAllClinics());
    }
    
    @PostMapping
    public ResponseEntity<Clinic> createClinic(@Valid @RequestBody CreateClinicRequest request) {
        // Create clinic admin user first
        User adminUser = new User();
        adminUser.setCi(request.getClinicAdmin().getCi());
        adminUser.setFirstName(request.getClinicAdmin().getFirstName());
        adminUser.setLastName(request.getClinicAdmin().getLastName());
        adminUser.setEmail(request.getClinicAdmin().getEmail());
        adminUser.setPhone(request.getClinicAdmin().getPhone());
        adminUser.setAddress(request.getClinicAdmin().getAddress());
        if (request.getClinicAdmin().getDateOfBirth() != null) {
            adminUser.setDateOfBirth(request.getClinicAdmin().getDateOfBirth().atStartOfDay());
        }
        // Note: Password and email verification will be set during sign-up
        
        // Create clinic with admin
        Clinic clinic = clinicService.createClinic(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getAddress(),
                adminUser
        );
        
        // Create clinic admin
        ClinicAdmin clinicAdmin = clinicAdminService.createClinicAdmin(adminUser);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(clinic);
    }
    
    @GetMapping("/{clinicName}")
    public ResponseEntity<Clinic> getClinicByName(@PathVariable String clinicName) {
        Clinic clinic = clinicService.findClinicByName(clinicName);
        if (clinic == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clinic);
    }
    
    @GetMapping("/{clinicName}/health-worker/{healthWorkerCi}")
    public ResponseEntity<HealthWorker> getHealthWorkerByCi(
            @PathVariable String clinicName,
            @PathVariable String healthWorkerCi) {
        HealthWorker healthWorker = healthWorkerService.findHealthWorkerByCi(healthWorkerCi, clinicName);
        if (healthWorker == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(healthWorker);
    }
}

