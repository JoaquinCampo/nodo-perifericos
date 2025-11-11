package com.nodosperifericos.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "account",
       uniqueConstraints = @UniqueConstraint(name = "uk_account_provider", columnNames = {"provider", "provider_account_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @GeneratedValue(generator = "cuid")
    @GenericGenerator(name = "cuid", strategy = "com.nodosperifericos.domain.CuidGenerator")
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_account_user"))
    private User user;
    
    @Column(nullable = false)
    private String type;
    
    @Column(nullable = false)
    private String provider;
    
    @Column(name = "provider_account_id", nullable = false)
    private String providerAccountId;
    
    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;
    
    @Column(name = "access_token", columnDefinition = "TEXT")
    private String accessToken;
    
    @Column(name = "expires_at")
    private Integer expiresAt;
    
    @Column(name = "token_type")
    private String tokenType;
    
    private String scope;
    
    @Column(name = "id_token", columnDefinition = "TEXT")
    private String idToken;
    
    @Column(name = "session_state")
    private String sessionState;
    
    @Column(name = "refresh_token_expires_in")
    private Integer refreshTokenExpiresIn;
}

