package com.olegf.thealthback.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainingStatsDto {
    private int totalTrainings;
    private int totalPrograms;
}
