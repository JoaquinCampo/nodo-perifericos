package com.nodosperifericos.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "configuration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
    
    @Id
    @GeneratedValue(generator = "cuid")
    @GenericGenerator(name = "cuid", strategy = "com.nodosperifericos.domain.CuidGenerator")
    private String id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false, unique = true,
                foreignKey = @ForeignKey(name = "fk_configuration_clinic"))
    private Clinic clinic;
    
    @Column(name = "portal_title", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'Portal de Clínica'")
    private String portalTitle = "Portal de Clínica";
    
    @Column(name = "sidebar_text_color", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '#111827'")
    private String sidebarTextColor = "#111827";
    
    @Column(name = "sidebar_background_color", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '#F1F5F9'")
    private String sidebarBackgroundColor = "#F1F5F9";
    
    @Column(name = "background_color", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '#F8FAFC'")
    private String backgroundColor = "#F8FAFC";
    
    @Column(name = "card_background_color", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '#FFFFFF'")
    private String cardBackgroundColor = "#FFFFFF";
    
    @Column(name = "card_text_color", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '#111827'")
    private String cardTextColor = "#111827";
    
    @Column(name = "icon_text_color", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '#FFFFFF'")
    private String iconTextColor = "#FFFFFF";
    
    @Column(name = "icon_background_color", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '#3B82F6'")
    private String iconBackgroundColor = "#3B82F6";
    
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

