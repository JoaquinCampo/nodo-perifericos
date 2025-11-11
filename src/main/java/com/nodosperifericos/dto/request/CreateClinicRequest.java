package com.nodosperifericos.dto.request;

import com.nodosperifericos.validation.Ci;
import com.nodosperifericos.validation.Phone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateClinicRequest {
    @NotBlank(message = "El nombre es requerido")
    private String name;
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no es válido")
    private String email;
    
    @NotBlank(message = "El teléfono es requerido")
    private String phone;
    
    @NotBlank(message = "La dirección es requerida")
    private String address;
    
    @Valid
    @NotNull(message = "El administrador de clínica es requerido")
    private ClinicAdminData clinicAdmin;
    
    @Data
    public static class ClinicAdminData {
        @NotBlank(message = "CI es requerido")
        @Ci
        private String ci;
        
        @NotBlank(message = "El nombre es requerido")
        private String firstName;
        
        @NotBlank(message = "El apellido es requerido")
        private String lastName;
        
        @NotBlank(message = "El email es requerido")
        @Email(message = "El email no es válido")
        private String email;
        
        @Phone
        private String phone;
        
        private String address;
        
        private LocalDate dateOfBirth;
    }
}

