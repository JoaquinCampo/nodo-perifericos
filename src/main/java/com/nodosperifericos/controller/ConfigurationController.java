package com.nodosperifericos.controller;

import com.nodosperifericos.domain.Configuration;
import com.nodosperifericos.dto.request.UpdateConfigurationRequest;
import com.nodosperifericos.service.ConfigurationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuration")
@RequiredArgsConstructor
public class ConfigurationController {
    
    private final ConfigurationService configurationService;
    
    @GetMapping
    public ResponseEntity<Configuration> getConfiguration(@RequestParam String clinicId) {
        Configuration config = configurationService.getConfiguration(clinicId);
        return ResponseEntity.ok(config);
    }
    
    @PutMapping("/{configurationId}")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<Configuration> updateConfiguration(
            @PathVariable String configurationId,
            @Valid @RequestBody UpdateConfigurationRequest request) {
        
        Configuration updatedConfig = new Configuration();
        updatedConfig.setPortalTitle(request.getPortalTitle());
        updatedConfig.setSidebarTextColor(request.getSidebarTextColor());
        updatedConfig.setSidebarBackgroundColor(request.getSidebarBackgroundColor());
        updatedConfig.setBackgroundColor(request.getBackgroundColor());
        updatedConfig.setIconTextColor(request.getIconTextColor());
        updatedConfig.setIconBackgroundColor(request.getIconBackgroundColor());
        updatedConfig.setCardBackgroundColor(request.getCardBackgroundColor());
        updatedConfig.setCardTextColor(request.getCardTextColor());
        
        Configuration config = configurationService.updateConfiguration(configurationId, updatedConfig);
        return ResponseEntity.ok(config);
    }
    
    @PostMapping("/{configurationId}/reset")
    @PreAuthorize("hasRole('CLINIC_ADMIN')")
    public ResponseEntity<Configuration> resetConfiguration(@PathVariable String configurationId) {
        Configuration config = configurationService.resetConfiguration(configurationId);
        return ResponseEntity.ok(config);
    }
}

