package com.nodosperifericos.service;

import com.nodosperifericos.domain.Configuration;
import com.nodosperifericos.domain.User;
import com.nodosperifericos.exception.ResourceNotFoundException;
import com.nodosperifericos.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigurationService {
    
    private final ConfigurationRepository configurationRepository;
    private final PermissionService permissionService;
    
    public Configuration getConfiguration(String clinicId) {
        return configurationRepository.findByClinicId(clinicId)
                .orElseThrow(() -> new ResourceNotFoundException("Configuración no encontrada", HttpStatus.NOT_FOUND));
    }
    
    @Transactional
    public Configuration updateConfiguration(String configurationId, Configuration updatedConfig) {
        permissionService.checkCanUpdateConfiguration(configurationId);
        
        Configuration config = configurationRepository.findById(configurationId)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada"));
        
        // Verify clinic ownership
        User currentUser = permissionService.getCurrentUser();
        if (!currentUser.getClinic().getId().equals(config.getClinic().getId())) {
            throw new RuntimeException("No tienes permisos para actualizar esta configuración");
        }
        
        if (updatedConfig.getPortalTitle() != null) {
            config.setPortalTitle(updatedConfig.getPortalTitle());
        }
        if (updatedConfig.getSidebarTextColor() != null) {
            config.setSidebarTextColor(updatedConfig.getSidebarTextColor());
        }
        if (updatedConfig.getSidebarBackgroundColor() != null) {
            config.setSidebarBackgroundColor(updatedConfig.getSidebarBackgroundColor());
        }
        if (updatedConfig.getBackgroundColor() != null) {
            config.setBackgroundColor(updatedConfig.getBackgroundColor());
        }
        if (updatedConfig.getCardBackgroundColor() != null) {
            config.setCardBackgroundColor(updatedConfig.getCardBackgroundColor());
        }
        if (updatedConfig.getCardTextColor() != null) {
            config.setCardTextColor(updatedConfig.getCardTextColor());
        }
        if (updatedConfig.getIconTextColor() != null) {
            config.setIconTextColor(updatedConfig.getIconTextColor());
        }
        if (updatedConfig.getIconBackgroundColor() != null) {
            config.setIconBackgroundColor(updatedConfig.getIconBackgroundColor());
        }
        
        return configurationRepository.save(config);
    }
    
    @Transactional
    public Configuration resetConfiguration(String configurationId) {
        permissionService.checkCanUpdateConfiguration(configurationId);
        
        Configuration config = configurationRepository.findById(configurationId)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada"));
        
        // Verify clinic ownership
        User currentUser = permissionService.getCurrentUser();
        if (!currentUser.getClinic().getId().equals(config.getClinic().getId())) {
            throw new RuntimeException("No tienes permisos para actualizar esta configuración");
        }
        
        config.setPortalTitle("Portal de Clínica");
        config.setSidebarTextColor("#111827");
        config.setSidebarBackgroundColor("#F1F5F9");
        config.setBackgroundColor("#F8FAFC");
        config.setCardBackgroundColor("#FFFFFF");
        config.setCardTextColor("#111827");
        config.setIconTextColor("#FFFFFF");
        config.setIconBackgroundColor("#3B82F6");
        
        return configurationRepository.save(config);
    }
}

