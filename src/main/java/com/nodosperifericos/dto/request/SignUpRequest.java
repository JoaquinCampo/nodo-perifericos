package com.nodosperifericos.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no es válido")
    private String email;
    
    @NotBlank(message = "La contraseña es requerida")
    private String password;
    
    @NotBlank(message = "El código de verificación es requerido")
    private String verificationCode;
    
    @NotBlank(message = "El token es requerido")
    private String token;
}

