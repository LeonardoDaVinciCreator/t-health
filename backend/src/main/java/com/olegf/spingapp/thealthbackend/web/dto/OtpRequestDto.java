package com.olegf.spingapp.thealthbackend.web.dto;

import lombok.Data;

@Data
public class OtpRequestDto {
    private String phone;
    private String code;
}
