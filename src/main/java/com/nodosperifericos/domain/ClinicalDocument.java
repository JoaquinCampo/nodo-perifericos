package com.nodosperifericos.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "clinical_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalDocument {
    
    @Id
    @GeneratedValue(generator = "cuid")
    @GenericGenerator(name = "cuid", strategy = "com.nodosperifericos.domain.CuidGenerator")
    private String id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "content_type")
    private String contentType;
    
    @Column(name = "content_url")
    private String contentUrl;
    
    @Column(name = "health_user_ci", nullable = false)
    private String healthUserCi;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_clinical_document_clinic"))
    private Clinic clinic;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_worker_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_clinical_document_health_worker"))
    private HealthWorker healthWorker;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

