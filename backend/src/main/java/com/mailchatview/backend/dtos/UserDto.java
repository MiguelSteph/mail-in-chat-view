package com.mailchatview.backend.dtos;

import lombok.Data;

@Data
public class UserDto {
    private String userId;
    private String email;
    private String name;
    private String pictureUrl;
    private String familyName;
}
