package com.olegf.thealthback.web.dto;

import com.olegf.thealthback.domain.entity.Exercise;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class TrainingCreateDto {
    private long userId;
    private LocalDateTime startDate;
    private List<ProgramDto> program;

    @Data
    @AllArgsConstructor
    public static class ProgramDto {
        private String title;
        private String description;
        private byte[] media;
        private Exercise.Timing timing;
    }
}
