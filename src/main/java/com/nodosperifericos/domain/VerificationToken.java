package com.nodosperifericos.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_token",
       uniqueConstraints = @UniqueConstraint(name = "uk_verification_token", columnNames = {"identifier", "token"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken {
    
    @Column(nullable = false)
    private String identifier;
    
    @Id
    @Column(nullable = false, unique = true)
    private String token;
    
    @Column(nullable = false)
    private LocalDateTime expires;
}

