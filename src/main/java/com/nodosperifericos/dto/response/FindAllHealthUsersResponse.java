package com.nodosperifericos.dto.response;

import com.nodosperifericos.integration.hcen.HcenHealthUserClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllHealthUsersResponse {
    private List<HcenHealthUserClient.HealthUser> items;
    private Integer page;
    private Integer size;
    private Integer totalItems;
    private Integer totalPages;
    private Boolean hasNext;
    private Boolean hasPrevious;
}

