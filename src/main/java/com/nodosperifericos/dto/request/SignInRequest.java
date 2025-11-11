package com.nodosperifericos.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {
    @NotBlank(message = "La clínica es requerida")
    private String clinicId;
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no es válido")
    private String email;
    
    @NotBlank(message = "La contraseña es requerida")
    private String password;
}

