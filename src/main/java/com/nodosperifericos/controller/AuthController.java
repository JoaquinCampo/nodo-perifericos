package com.nodosperifericos.controller;

import com.nodosperifericos.domain.Session;
import com.nodosperifericos.domain.User;
import com.nodosperifericos.dto.request.SendVerificationEmailRequest;
import com.nodosperifericos.dto.request.SignInRequest;
import com.nodosperifericos.dto.request.SignUpRequest;
import com.nodosperifericos.dto.response.AuthResponse;
import com.nodosperifericos.dto.response.SendVerificationEmailResponse;
import com.nodosperifericos.repository.ClinicRepository;
import com.nodosperifericos.repository.UserRepository;
import com.nodosperifericos.security.JwtTokenProvider;
import com.nodosperifericos.service.AuthService;
import com.nodosperifericos.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;
    
    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody SignInRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail() + ":" + request.getClinicId(),
                            request.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            User user = authService.findUserByEmailAndClinicId(request.getEmail(), request.getClinicId());
            Session session = authService.createSession(user.getId());
            
            AuthResponse response = AuthResponse.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .clinicId(user.getClinic().getId())
                    .clinicName(user.getClinic().getName())
                    .sessionToken(session.getSessionToken())
                    .isClinicAdmin(user.getClinicAdmin() != null)
                    .isHealthWorker(user.getHealthWorker() != null)
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Clínica, email o contraseña incorrectos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
    
    @PostMapping("/send-verification-code")
    public ResponseEntity<SendVerificationEmailResponse> sendVerificationCode(
            @Valid @RequestBody SendVerificationEmailRequest request) {
        try {
            User user = authService.findUserByEmailAndClinicId(request.getEmail(), request.getClinicId());
            
            if (user.getEmailVerified() != null) {
                return ResponseEntity.badRequest().build();
            }
            
            // Generate 6-digit OTP
            SecureRandom random = new SecureRandom();
            int otp = 100000 + random.nextInt(900000);
            String verificationCode = String.valueOf(otp);
            
            // Hash the verification code
            String hashedVerificationCode = passwordEncoder.encode(verificationCode);
            
            // Generate JWT token
            String token = jwtTokenProvider.generateToken(
                    request.getEmail(),
                    request.getClinicId(),
                    hashedVerificationCode
            );
            
            // Send email
            String clinicName = clinicRepository.findById(request.getClinicId())
                    .map(clinic -> clinic.getName())
                    .orElse("Nodos Periféricos");
            
            emailService.sendVerificationEmail(request.getEmail(), verificationCode, clinicName);
            
            SendVerificationEmailResponse response = new SendVerificationEmailResponse();
            response.setToken(token);
            response.setMessage("Verification email sent successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        try {
            // Validate token
            if (!jwtTokenProvider.validateToken(request.getToken())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Token inválido o expirado");
                return ResponseEntity.badRequest().body(error);
            }
            
            String email = jwtTokenProvider.getEmailFromToken(request.getToken());
            String clinicId = jwtTokenProvider.getClinicIdFromToken(request.getToken());
            String hashedVerificationCode = jwtTokenProvider.getHashedVerificationCodeFromToken(request.getToken());
            
            if (!email.equals(request.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email no coincide");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Verify OTP
            if (!passwordEncoder.matches(request.getVerificationCode(), hashedVerificationCode)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Código de verificación inválido");
                return ResponseEntity.badRequest().body(error);
            }
            
            User user = authService.findUserByEmailAndClinicId(email, clinicId);
            
            if (user.getEmailVerified() != null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuario con este email ya verificado");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Set password and verify email
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmailVerified(LocalDateTime.now());
            userRepository.save(user);
            
            // Create session
            Session session = authService.createSession(user.getId());
            
            AuthResponse response = AuthResponse.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .clinicId(user.getClinic().getId())
                    .clinicName(user.getClinic().getName())
                    .sessionToken(session.getSessionToken())
                    .isClinicAdmin(user.getClinicAdmin() != null)
                    .isHealthWorker(user.getHealthWorker() != null)
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

