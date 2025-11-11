package com.nodosperifericos.service;

import com.nodosperifericos.domain.ClinicAdmin;
import com.nodosperifericos.domain.HealthWorker;
import com.nodosperifericos.domain.User;
import com.nodosperifericos.exception.UnauthorizedException;
import com.nodosperifericos.repository.ClinicAdminRepository;
import com.nodosperifericos.repository.HealthWorkerRepository;
import com.nodosperifericos.repository.SessionRepository;
import com.nodosperifericos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionService {
    
    private final HealthWorkerRepository healthWorkerRepository;
    private final ClinicAdminRepository clinicAdminRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Usuario no autenticado");
        }
        
        String username = authentication.getName();
        String[] parts = username.split(":");
        if (parts.length != 2) {
            throw new UnauthorizedException("Formato de usuario inválido");
        }
        
        String email = parts[0];
        String clinicId = parts[1];
        
        return userRepository.findByEmailAndClinicId(email, clinicId)
                .orElseThrow(() -> new UnauthorizedException("Usuario no encontrado"));
    }
    
    public void checkCanAccessClinic(String clinicId) {
        User currentUser = getCurrentUser();
        if (!currentUser.getClinic().getId().equals(clinicId)) {
            throw new UnauthorizedException("No tienes permisos para ver los profesionales de esta clínica");
        }
    }
    
    public void checkCanCreateHealthWorker(String clinicId) {
        User currentUser = getCurrentUser();
        if (currentUser.getClinicAdmin() == null || !currentUser.getClinic().getId().equals(clinicId)) {
            throw new UnauthorizedException("No tienes permisos para crear un profesional en esta clínica");
        }
    }
    
    public void checkCanUpdateHealthWorker(String healthWorkerId) {
        User currentUser = getCurrentUser();
        
        if (currentUser.getClinicAdmin() == null && 
            (currentUser.getHealthWorker() == null || !currentUser.getHealthWorker().getId().equals(healthWorkerId))) {
            throw new UnauthorizedException("No tienes permisos para actualizar este profesional");
        }
        
        HealthWorker healthWorker = healthWorkerRepository.findById(healthWorkerId)
                .orElseThrow(() -> new RuntimeException("Profesional de salud no encontrado"));
        
        if (!currentUser.getClinic().getId().equals(healthWorker.getUser().getClinic().getId())) {
            throw new UnauthorizedException("No tienes permisos para actualizar este profesional");
        }
    }
    
    public void checkCanDeleteHealthWorker(String healthWorkerId) {
        User currentUser = getCurrentUser();
        
        if (currentUser.getClinicAdmin() == null) {
            throw new UnauthorizedException("No tienes permisos para eliminar este profesional");
        }
        
        HealthWorker healthWorker = healthWorkerRepository.findById(healthWorkerId)
                .orElseThrow(() -> new RuntimeException("Profesional de salud no encontrado"));
        
        if (!currentUser.getClinic().getId().equals(healthWorker.getUser().getClinic().getId())) {
            throw new UnauthorizedException("No tienes permisos para eliminar este profesional");
        }
    }
    
    public void checkCanFindAllClinicAdmins(String clinicId) {
        User currentUser = getCurrentUser();
        if (!currentUser.getClinic().getId().equals(clinicId)) {
            throw new UnauthorizedException("No tienes permisos para ver los administradores de esta clínica");
        }
    }
    
    public void checkCanCreateClinicAdmin(String clinicId) {
        User currentUser = getCurrentUser();
        if (!currentUser.getClinic().getId().equals(clinicId)) {
            throw new UnauthorizedException("No tienes permisos para crear administradores en esta clínica");
        }
    }
    
    public void checkCanUpdateClinicAdmin(String clinicAdminId) {
        User currentUser = getCurrentUser();
        
        if (currentUser.getClinicAdmin() == null) {
            throw new UnauthorizedException("No tienes permisos para actualizar este administrador");
        }
        
        ClinicAdmin clinicAdmin = clinicAdminRepository.findById(clinicAdminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        
        if (!currentUser.getClinic().getId().equals(clinicAdmin.getUser().getClinic().getId())) {
            throw new UnauthorizedException("No tienes permisos para actualizar este administrador");
        }
    }
    
    public void checkCanDeleteClinicAdmin(String clinicAdminId) {
        User currentUser = getCurrentUser();
        
        if (currentUser.getClinicAdmin() == null) {
            throw new UnauthorizedException("No tienes permisos para eliminar este administrador");
        }
        
        ClinicAdmin clinicAdmin = clinicAdminRepository.findById(clinicAdminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        
        if (!currentUser.getClinic().getId().equals(clinicAdmin.getUser().getClinic().getId())) {
            throw new UnauthorizedException("No tienes permisos para eliminar este administrador");
        }
    }
    
    public void checkCanUpdateConfiguration(String configurationId) {
        User currentUser = getCurrentUser();
        
        if (currentUser.getClinicAdmin() == null) {
            throw new UnauthorizedException("No tienes permisos para actualizar esta configuración");
        }
        
        // Note: Configuration clinic ownership is checked in ConfigurationService
        // by fetching the configuration and comparing clinicId
    }
    
    public void checkIsClinicAdmin(String clinicId) {
        User currentUser = getCurrentUser();
        if (currentUser.getClinicAdmin() == null || !currentUser.getClinic().getId().equals(clinicId)) {
            throw new UnauthorizedException("No tienes permisos de administrador");
        }
    }
}
