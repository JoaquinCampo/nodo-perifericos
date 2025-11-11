package com.nodosperifericos.service;

import com.nodosperifericos.domain.HealthWorker;
import com.nodosperifericos.domain.HealthWorkerSpeciality;
import com.nodosperifericos.domain.Speciality;
import com.nodosperifericos.domain.User;
import com.nodosperifericos.exception.ConflictException;
import com.nodosperifericos.exception.ResourceNotFoundException;
import com.nodosperifericos.repository.HealthWorkerRepository;
import com.nodosperifericos.repository.SpecialityRepository;
import com.nodosperifericos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthWorkerService {
    
    private final HealthWorkerRepository healthWorkerRepository;
    private final UserRepository userRepository;
    private final SpecialityRepository specialityRepository;
    private final PermissionService permissionService;
    
    public List<HealthWorker> findAllHealthWorkers(String clinicId) {
        permissionService.checkCanAccessClinic(clinicId);
        return healthWorkerRepository.findByUserClinicId(clinicId);
    }
    
    public HealthWorker findHealthWorkerByCi(String ci, String clinicName) {
        return healthWorkerRepository.findByCiAndClinicName(ci, clinicName)
                .orElse(null);
    }
    
    @Transactional
    public HealthWorker createHealthWorker(User user, List<String> specialityNames) {
        permissionService.checkCanCreateHealthWorker(user.getClinic().getId());
        
        // Check for existing user
        User existing = userRepository.findByClinicIdAndEmailOrCiOrPhone(
                user.getClinic().getId(),
                user.getEmail(),
                user.getCi(),
                user.getPhone()
        ).orElse(null);
        
        if (existing != null) {
            throw new ConflictException("Ya existe un usuario con estos datos en la clínica");
        }
        
        HealthWorker healthWorker = new HealthWorker();
        healthWorker.setUser(user);
        
        if (specialityNames != null && !specialityNames.isEmpty()) {
            List<HealthWorkerSpeciality> specialities = specialityNames.stream()
                    .map(name -> {
                        Speciality speciality = specialityRepository.findByNameIgnoreCase(name.toLowerCase())
                                .orElseGet(() -> {
                                    Speciality newSpeciality = new Speciality();
                                    newSpeciality.setName(name.toLowerCase());
                                    return specialityRepository.save(newSpeciality);
                                });
                        
                        HealthWorkerSpeciality hws = new HealthWorkerSpeciality();
                        hws.setHealthWorker(healthWorker);
                        hws.setSpeciality(speciality);
                        return hws;
                    })
                    .collect(Collectors.toList());
            
            healthWorker.setHealthWorkerSpecialities(specialities);
        }
        
        return healthWorkerRepository.save(healthWorker);
    }
    
    @Transactional
    public HealthWorker updateHealthWorker(String healthWorkerId, User user, List<String> specialityNames) {
        permissionService.checkCanUpdateHealthWorker(healthWorkerId);
        
        HealthWorker healthWorker = healthWorkerRepository.findById(healthWorkerId)
                .orElseThrow(() -> new RuntimeException("Profesional de salud no encontrado"));
        
        // Check for existing user with same email/ci/phone (excluding current user)
        User existing = userRepository.findByClinicIdAndEmailOrCiOrPhone(
                healthWorker.getUser().getClinic().getId(),
                user.getEmail(),
                user.getCi(),
                user.getPhone()
        ).orElse(null);
        
        if (existing != null && !existing.getId().equals(healthWorker.getUser().getId())) {
            throw new ConflictException("Ya existe un usuario con estos datos en la clínica");
        }
        
        // Update user fields
        User currentUser = healthWorker.getUser();
        if (user.getCi() != null) currentUser.setCi(user.getCi());
        if (user.getFirstName() != null) currentUser.setFirstName(user.getFirstName());
        if (user.getLastName() != null) currentUser.setLastName(user.getLastName());
        if (user.getEmail() != null) currentUser.setEmail(user.getEmail());
        if (user.getPhone() != null) currentUser.setPhone(user.getPhone());
        if (user.getAddress() != null) currentUser.setAddress(user.getAddress());
        if (user.getDateOfBirth() != null) currentUser.setDateOfBirth(user.getDateOfBirth());
        userRepository.save(currentUser);
        
        // Update specialities
        healthWorker.getHealthWorkerSpecialities().clear();
        if (specialityNames != null && !specialityNames.isEmpty()) {
            List<HealthWorkerSpeciality> specialities = specialityNames.stream()
                    .map(name -> {
                        Speciality speciality = specialityRepository.findByNameIgnoreCase(name.toLowerCase())
                                .orElseGet(() -> {
                                    Speciality newSpeciality = new Speciality();
                                    newSpeciality.setName(name.toLowerCase());
                                    return specialityRepository.save(newSpeciality);
                                });
                        
                        HealthWorkerSpeciality hws = new HealthWorkerSpeciality();
                        hws.setHealthWorker(healthWorker);
                        hws.setSpeciality(speciality);
                        return hws;
                    })
                    .collect(Collectors.toList());
            
            healthWorker.setHealthWorkerSpecialities(specialities);
        }
        
        return healthWorkerRepository.save(healthWorker);
    }
    
    @Transactional
    public void deleteHealthWorker(String healthWorkerId) {
        permissionService.checkCanDeleteHealthWorker(healthWorkerId);
        
        HealthWorker healthWorker = healthWorkerRepository.findById(healthWorkerId)
                .orElseThrow(() -> new RuntimeException("Profesional de salud no encontrado"));
        
        userRepository.delete(healthWorker.getUser());
    }
}

