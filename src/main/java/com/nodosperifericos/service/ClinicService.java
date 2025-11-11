package com.nodosperifericos.service;

import com.nodosperifericos.domain.Clinic;
import com.nodosperifericos.domain.ClinicAdmin;
import com.nodosperifericos.domain.Configuration;
import com.nodosperifericos.domain.User;
import com.nodosperifericos.repository.ClinicRepository;
import com.nodosperifericos.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicService {
    
    private final ClinicRepository clinicRepository;
    private final ConfigurationRepository configurationRepository;
    
    public List<Clinic> findAllClinics() {
        return clinicRepository.findAll();
    }
    
    public Clinic findClinicByName(String name) {
        return clinicRepository.findByName(name)
                .orElse(null);
    }
    
    @Transactional
    public Clinic createClinic(String name, String email, String phone, String address, User clinicAdminUser) {
        Clinic clinic = new Clinic();
        clinic.setName(name);
        clinic.setEmail(email);
        clinic.setPhone(phone);
        clinic.setAddress(address);
        clinic.getUsers().add(clinicAdminUser);
        clinicAdminUser.setClinic(clinic);
        
        Clinic savedClinic = clinicRepository.save(clinic);
        
        // Create default configuration
        Configuration config = new Configuration();
        config.setClinic(savedClinic);
        configurationRepository.save(config);
        
        return savedClinic;
    }
}

