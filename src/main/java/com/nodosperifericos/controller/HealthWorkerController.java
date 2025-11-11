package com.nodosperifericos.controller;

import com.nodosperifericos.domain.Clinic;
import com.nodosperifericos.domain.HealthWorker;
import com.nodosperifericos.domain.User;
import com.nodosperifericos.dto.request.CreateHealthWorkerRequest;
import com.nodosperifericos.dto.request.UpdateHealthWorkerRequest;
import com.nodosperifericos.repository.ClinicRepository;
import com.nodosperifericos.repository.HealthWorkerRepository;
import com.nodosperifericos.repository.UserRepository;
import com.nodosperifericos.service.HealthWorkerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/health-workers")
@RequiredArgsConstructor
public class HealthWorkerController {

    private final HealthWorkerService healthWorkerService;
    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final HealthWorkerRepository healthWorkerRepository;

    @GetMapping
    public ResponseEntity<List<HealthWorker>> getAllHealthWorkers(@RequestParam String clinicId) {
        List<HealthWorker> healthWorkers = healthWorkerService.findAllHealthWorkers(clinicId);
        return ResponseEntity.ok(healthWorkers);
    }

    @PostMapping
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<HealthWorker> createHealthWorker(@Valid @RequestBody CreateHealthWorkerRequest request) {
        // Create user
        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new com.nodosperifericos.exception.ResourceNotFoundException("Clínica no encontrada",
                        org.springframework.http.HttpStatus.NOT_FOUND));

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

        // Create health worker (permission check and duplicate check is inside service)
        HealthWorker healthWorker = healthWorkerService.createHealthWorker(user, request.getSpecialities());
        return ResponseEntity.status(HttpStatus.CREATED).body(healthWorker);
    }

    @PutMapping("/{healthWorkerId}")
    @PreAuthorize("hasRole('CLINIC_ADMIN') or hasRole('HEALTH_WORKER')")
    public ResponseEntity<HealthWorker> updateHealthWorker(
            @PathVariable String healthWorkerId,
            @Valid @RequestBody UpdateHealthWorkerRequest request) {

        HealthWorker existing = healthWorkerRepository.findById(healthWorkerId)
                .orElseThrow(() -> new com.nodosperifericos.exception.ResourceNotFoundException(
                        "Profesional de salud no encontrado", org.springframework.http.HttpStatus.NOT_FOUND));

        User user = existing.getUser();
        if (request.getCi() != null)
            user.setCi(request.getCi());
        if (request.getFirstName() != null)
            user.setFirstName(request.getFirstName());
        if (request.getLastName() != null)
            user.setLastName(request.getLastName());
        if (request.getEmail() != null)
            user.setEmail(request.getEmail());
        if (request.getPhone() != null)
            user.setPhone(request.getPhone());
        if (request.getAddress() != null)
            user.setAddress(request.getAddress());
        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth().atStartOfDay());
        }
        userRepository.save(user);

        // Permission check is inside service
        HealthWorker updated = healthWorkerService.updateHealthWorker(healthWorkerId, user, request.getSpecialities());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{healthWorkerId}")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<Void> deleteHealthWorker(@PathVariable String healthWorkerId) {
        // Permission check is inside service
        healthWorkerService.deleteHealthWorker(healthWorkerId);
        return ResponseEntity.noContent().build();
    }
}
