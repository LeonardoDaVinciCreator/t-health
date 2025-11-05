package com.olegf.thealthback.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class ActivityCreateDto {
    private long userId;
    private int steps;
    private int value;
    private List<String> goals;
    private double calories;
}
