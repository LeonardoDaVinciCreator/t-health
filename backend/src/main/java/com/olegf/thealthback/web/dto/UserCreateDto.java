package com.olegf.thealthback.web.dto;

import lombok.Data;

@Data
public class UserCreateDto {
    private String username;
    private String phone;
    private String position;
    private String department;
    private String avatarUrl;
}
