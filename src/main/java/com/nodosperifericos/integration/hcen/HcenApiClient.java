package com.nodosperifericos.integration.hcen;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HcenApiClient {
    
    @Value("${hcen.api.base-url}")
    private String baseUrl;
    
    @Value("${hcen.api.username}")
    private String username;
    
    @Value("${hcen.api.password}")
    private String password;
    
    private final RestTemplate restTemplate;
    
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
    
    public <T> T get(String path, Class<T> responseType, Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/api/" + path);
        if (queryParams != null && !queryParams.isEmpty()) {
            queryParams.forEach(builder::queryParam);
        }
        
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<T> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                responseType
        );
        return response.getBody();
    }
    
    public <T> T post(String path, Object body, Class<T> responseType) {
        HttpEntity<Object> entity = new HttpEntity<>(body, createHeaders());
        ResponseEntity<T> response = restTemplate.exchange(
                baseUrl + "/api/" + path,
                HttpMethod.POST,
                entity,
                responseType
        );
        return response.getBody();
    }
}

