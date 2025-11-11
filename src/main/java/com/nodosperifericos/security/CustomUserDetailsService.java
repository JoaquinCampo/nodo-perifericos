package com.nodosperifericos.security;

import com.nodosperifericos.domain.User;
import com.nodosperifericos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Username format: email:clinicId
        String[] parts = username.split(":");
        if (parts.length != 2) {
            throw new UsernameNotFoundException("Invalid username format");
        }
        
        String email = parts[0];
        String clinicId = parts[1];
        
        User user = userRepository.findByEmailAndClinicId(email, clinicId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return buildUserDetails(user);
    }
    
    @Transactional(readOnly = true)
    public UserDetails loadUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return buildUserDetails(user);
    }
    
    private UserDetails buildUserDetails(User user) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        
        if (user.getClinicAdmin() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CLINIC_ADMIN"));
        }
        if (user.getHealthWorker() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_HEALTH_WORKER"));
        }
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail() + ":" + user.getClinic().getId())
                .password(user.getPassword() != null ? user.getPassword() : "")
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(user.getEmailVerified() == null)
                .build();
    }
}

