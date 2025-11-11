package com.nodosperifericos.service;

import com.nodosperifericos.domain.ClinicAdmin;
import com.nodosperifericos.domain.User;
import com.nodosperifericos.exception.ConflictException;
import com.nodosperifericos.exception.ResourceNotFoundException;
import com.nodosperifericos.repository.ClinicAdminRepository;
import com.nodosperifericos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicAdminService {
    
    private final ClinicAdminRepository clinicAdminRepository;
    private final UserRepository userRepository;
    private final PermissionService permissionService;
    
    public List<ClinicAdmin> findAllClinicAdmins(String clinicId) {
        permissionService.checkCanFindAllClinicAdmins(clinicId);
        return clinicAdminRepository.findByClinicId(clinicId);
    }
    
    @Transactional
    public ClinicAdmin createClinicAdmin(User user) {
        permissionService.checkCanCreateClinicAdmin(user.getClinic().getId());
        
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
        
        ClinicAdmin clinicAdmin = new ClinicAdmin();
        clinicAdmin.setUser(user);
        return clinicAdminRepository.save(clinicAdmin);
    }
    
    @Transactional
    public ClinicAdmin updateClinicAdmin(String clinicAdminId, User user) {
        permissionService.checkCanUpdateClinicAdmin(clinicAdminId);
        
        ClinicAdmin clinicAdmin = clinicAdminRepository.findById(clinicAdminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        
        // Check for existing user with same email/ci/phone (excluding current user)
        User existing = userRepository.findByClinicIdAndEmailOrCiOrPhone(
                clinicAdmin.getUser().getClinic().getId(),
                user.getEmail(),
                user.getCi(),
                user.getPhone()
        ).orElse(null);
        
        if (existing != null && !existing.getId().equals(clinicAdmin.getUser().getId())) {
            throw new ConflictException("Ya existe un usuario con estos datos en la clínica");
        }
        
        // Update user fields
        User currentUser = clinicAdmin.getUser();
        if (user.getFirstName() != null) currentUser.setFirstName(user.getFirstName());
        if (user.getLastName() != null) currentUser.setLastName(user.getLastName());
        if (user.getCi() != null) currentUser.setCi(user.getCi());
        if (user.getEmail() != null) currentUser.setEmail(user.getEmail());
        if (user.getPhone() != null) currentUser.setPhone(user.getPhone());
        if (user.getAddress() != null) currentUser.setAddress(user.getAddress());
        if (user.getDateOfBirth() != null) currentUser.setDateOfBirth(user.getDateOfBirth());
        userRepository.save(currentUser);
        
        return clinicAdmin;
    }
    
    @Transactional
    public void deleteClinicAdmin(String clinicAdminId) {
        permissionService.checkCanDeleteClinicAdmin(clinicAdminId);
        
        ClinicAdmin clinicAdmin = clinicAdminRepository.findById(clinicAdminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        
        userRepository.delete(clinicAdmin.getUser());
    }
}

