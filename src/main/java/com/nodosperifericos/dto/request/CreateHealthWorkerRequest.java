package com.nodosperifericos.dto.request;

import com.nodosperifericos.validation.Ci;
import com.nodosperifericos.validation.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateHealthWorkerRequest {
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
    
    @NotBlank(message = "La clínica es requerida")
    private String clinicId;
    
    private List<String> specialities;
}

