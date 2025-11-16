package com.olegf.thealthback.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("training")
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    @Id
    private Long id;
    private long userId;
    private String title;
    private String type;
    private long durationSecs;
    private int calories;
    private LocalDateTime date;

    public Training(long userId, String title, String type, long duration, int calories, LocalDateTime date) {
        this.userId = userId;
        this.title = title;
        this.type = type;
        this.durationSecs = duration;
        this.calories = calories;
        this.date = date;
    }

    public void doUpdate(TrainingApi.UpdateDto updateDto) {
        this.title = updateDto.getTitle();
        this.type = updateDto.getType();
        this.durationSecs = updateDto.getDurationSecs();
        this.calories = updateDto.getCalories();
    }

    public static Training from(TrainingApi.CreateDto createDto) {
        return new Training(
                createDto.getUserId(),
                createDto.getTitle(),
                createDto.getType(),
                createDto.getDurationSecs(),
                createDto.getCalories(),
                createDto.getDate()
        );
    }
}