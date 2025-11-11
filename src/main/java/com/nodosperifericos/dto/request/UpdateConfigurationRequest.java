package com.nodosperifericos.dto.request;

import lombok.Data;

@Data
public class UpdateConfigurationRequest {
    private String portalTitle;
    private String sidebarTextColor;
    private String sidebarBackgroundColor;
    private String backgroundColor;
    private String iconTextColor;
    private String iconBackgroundColor;
    private String cardBackgroundColor;
    private String cardTextColor;
}

