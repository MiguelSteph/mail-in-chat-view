package com.mailchatview.backend.dtos;

import lombok.Data;

@Data
public class GoogleClientInfo {
    private String clientId;
    private String scope;
}
