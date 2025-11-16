package com.olegf.thealthback.web.dto;

import com.olegf.thealthback.domain.entity.Activity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ActivityCreateDto {
    private long userId;
    private Activity.Type type;
    private BigDecimal value;
    private double calories;
}
