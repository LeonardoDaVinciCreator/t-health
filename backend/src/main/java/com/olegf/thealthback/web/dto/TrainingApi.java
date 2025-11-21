package com.olegf.thealthback.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Valid
public sealed class TrainingApi permits TrainingApi.CreateDto, TrainingApi.SearchCriteria, TrainingApi.UpdateDto {

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static final class CreateDto extends TrainingApi {
        @NotNull private long userId;
        @NotNull private String type;
        @NotNull private long durationSecs;
        @NotNull private LocalDateTime date;
        private String title;
        private int calories;
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static final class UpdateDto extends TrainingApi {
        private String type;
        private long durationSecs;
        private LocalDateTime date;
        private String title;
        private int calories;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static final class SearchCriteria extends TrainingApi {
        private String type;
    }
}
