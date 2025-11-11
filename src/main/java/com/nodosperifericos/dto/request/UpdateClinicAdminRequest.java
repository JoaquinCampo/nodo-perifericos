package com.nodosperifericos.dto.request;

import com.nodosperifericos.validation.Ci;
import com.nodosperifericos.validation.Phone;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateClinicAdminRequest {
    private String firstName;
    
    private String lastName;
    
    @Ci
    private String ci;
    
    @Email(message = "El email no es válido")
    private String email;
    
    @Phone
    private String phone;
    
    private String address;
    
    private LocalDate dateOfBirth;
}

