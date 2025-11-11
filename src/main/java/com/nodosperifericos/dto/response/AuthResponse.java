package com.nodosperifericos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String userId;
    private String email;
    private String clinicId;
    private String clinicName;
    private String sessionToken;
    private boolean isClinicAdmin;
    private boolean isHealthWorker;
}

