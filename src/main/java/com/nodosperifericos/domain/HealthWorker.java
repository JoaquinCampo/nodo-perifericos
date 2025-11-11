package com.nodosperifericos.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "health_worker")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthWorker {
    
    @Id
    @GeneratedValue(generator = "cuid")
    @GenericGenerator(name = "cuid", strategy = "com.nodosperifericos.domain.CuidGenerator")
    private String id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true, 
                foreignKey = @ForeignKey(name = "fk_health_worker_user"))
    private User user;
    
    @OneToMany(mappedBy = "healthWorker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HealthWorkerSpeciality> healthWorkerSpecialities = new ArrayList<>();
    
    @OneToMany(mappedBy = "healthWorker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClinicalDocument> clinicalDocuments = new ArrayList<>();
    
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

