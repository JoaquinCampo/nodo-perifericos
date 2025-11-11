package com.nodosperifericos.controller;

import com.nodosperifericos.domain.Clinic;
import com.nodosperifericos.domain.ClinicAdmin;
import com.nodosperifericos.domain.User;
import com.nodosperifericos.dto.request.CreateClinicAdminRequest;
import com.nodosperifericos.dto.request.UpdateClinicAdminRequest;
import com.nodosperifericos.repository.ClinicAdminRepository;
import com.nodosperifericos.repository.ClinicRepository;
import com.nodosperifericos.repository.UserRepository;
import com.nodosperifericos.service.ClinicAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/clinic-admins")
@RequiredArgsConstructor
public class ClinicAdminController {
    
    private final ClinicAdminService clinicAdminService;
    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final ClinicAdminRepository clinicAdminRepository;
    
    @GetMapping
    public ResponseEntity<List<ClinicAdmin>> getAllClinicAdmins(@RequestParam String clinicId) {
        List<ClinicAdmin> admins = clinicAdminService.findAllClinicAdmins(clinicId);
        return ResponseEntity.ok(admins);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<ClinicAdmin> createClinicAdmin(@Valid @RequestBody CreateClinicAdminRequest request) {
        // Create user
        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new com.nodosperifericos.exception.ResourceNotFoundException("Clínica no encontrada", org.springframework.http.HttpStatus.NOT_FOUND));
        
        User user = new User();
        user.setClinic(clinic);
        user.setCi(request.getCi());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth().atStartOfDay());
        }
        user = userRepository.save(user);
        
        // Create clinic admin (permission check and duplicate check is inside service)
        ClinicAdmin clinicAdmin = clinicAdminService.createClinicAdmin(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(clinicAdmin);
    }
    
    @PutMapping("/{clinicAdminId}")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<ClinicAdmin> updateClinicAdmin(
            @PathVariable String clinicAdminId,
            @Valid @RequestBody UpdateClinicAdminRequest request) {
        
        ClinicAdmin existing = clinicAdminRepository.findById(clinicAdminId)
                .orElseThrow(() -> new com.nodosperifericos.exception.ResourceNotFoundException("Administrador no encontrado", org.springframework.http.HttpStatus.NOT_FOUND));
        
        User user = existing.getUser();
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getCi() != null) user.setCi(request.getCi());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth().atStartOfDay());
        }
        userRepository.save(user);
        
        // Permission check is inside service
        clinicAdminService.updateClinicAdmin(clinicAdminId, user);
        return ResponseEntity.ok(existing);
    }
    
    @DeleteMapping("/{clinicAdminId}")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<Void> deleteClinicAdmin(@PathVariable String clinicAdminId) {
        // Permission check is inside service
        clinicAdminService.deleteClinicAdmin(clinicAdminId);
        return ResponseEntity.noContent().build();
    }
}

