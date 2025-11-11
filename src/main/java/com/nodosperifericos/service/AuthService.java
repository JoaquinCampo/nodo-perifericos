package com.nodosperifericos.service;

import com.nodosperifericos.domain.Clinic;
import com.nodosperifericos.domain.Session;
import com.nodosperifericos.domain.User;
import com.nodosperifericos.repository.ClinicRepository;
import com.nodosperifericos.repository.SessionRepository;
import com.nodosperifericos.repository.UserRepository;
import com.nodosperifericos.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    
    public User findUserByEmailAndClinicId(String email, String clinicId) {
        return userRepository.findByEmailAndClinicId(email, clinicId)
                .orElseThrow(() -> new RuntimeException("User with this email not found for this clinic"));
    }
    
    public User findUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with this ID not found"));
    }
    
    @Transactional
    public Session createSession(String userId) {
        String sessionToken = UUID.randomUUID().toString();
        LocalDateTime expires = LocalDateTime.now().plusDays(30);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Session session = new Session();
        session.setSessionToken(sessionToken);
        session.setUser(user);
        session.setExpires(expires);
        
        return sessionRepository.save(session);
    }
    
    @Transactional
    public void deleteSession(String sessionToken) {
        sessionRepository.deleteBySessionToken(sessionToken);
    }
    
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

