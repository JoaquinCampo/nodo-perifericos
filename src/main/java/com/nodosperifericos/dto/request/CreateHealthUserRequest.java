package com.nodosperifericos.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateHealthUserRequest {
    @NotBlank(message = "CI es requerido")
    @Size(min = 1, message = "CI es requerido")
    private String ci;
    
    @NotBlank(message = "Nombre es requerido")
    @Size(max = 50, message = "Nombre debe tener menos de 50 caracteres")
    private String firstName;
    
    @NotBlank(message = "Apellido es requerido")
    @Size(max = 50, message = "Apellido debe tener menos de 50 caracteres")
    private String lastName;
    
    @NotNull(message = "Género es requerido")
    private String gender; // MALE, FEMALE, OTHER
    
    @NotBlank(message = "Email es requerido")
    @Email(message = "Email debe tener un formato válido")
    private String email;
    
    private String phone;
    
    @Size(max = 200, message = "Dirección debe tener menos de 200 caracteres")
    private String address;
    
    @NotNull(message = "Fecha de nacimiento es requerida")
    private LocalDate dateOfBirth;
    
    @NotNull(message = "Al menos una clínica es requerida")
    @Size(min = 1, message = "Al menos una clínica es requerida")
    private List<String> clinicNames;
}

