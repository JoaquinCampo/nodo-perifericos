package com.nodosperifericos.controller;

import com.nodosperifericos.domain.ClinicalDocument;
import com.nodosperifericos.service.HealthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clinical-history")
@RequiredArgsConstructor
public class ClinicalHistoryController {
    
    private final HealthUserService healthUserService;
    
    @GetMapping("/{healthUserCi}")
    public ResponseEntity<?> getClinicalHistory(@PathVariable String healthUserCi) {
        List<ClinicalDocument> history = healthUserService.findClinicalHistory(healthUserCi);
        if (history == null || history.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Historial clínico no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(history);
    }
}

