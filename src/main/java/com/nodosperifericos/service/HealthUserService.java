package com.nodosperifericos.service;

import com.nodosperifericos.domain.ClinicalDocument;
import com.nodosperifericos.repository.ClinicalDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthUserService {
    
    private final ClinicalDocumentRepository clinicalDocumentRepository;
    
    public List<ClinicalDocument> findClinicalHistory(String healthUserCi) {
        return clinicalDocumentRepository.findByHealthUserCi(healthUserCi);
    }
}

