package com.nodosperifericos.dto.response;

import lombok.Data;

@Data
public class SendVerificationEmailResponse {
    private String token;
    private String message;
}

