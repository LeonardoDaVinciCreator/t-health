package com.olegf.thealthback.domain.entity;

import com.olegf.thealthback.web.dto.ActivityCreateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Table("activities")
@AllArgsConstructor
public class Activity {
    @Id
    private Long id;
    @Column("user_id")
    private Long userId;
    private BigDecimal value;
    private Type type;
    private double calories;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    public static Activity from(ActivityCreateDto createDto) {
        return new Activity(
                null,
                createDto.getUserId(),
                createDto.getValue(),
                createDto.getType(),
                createDto.getCalories(),
                LocalDateTime.now()
        );
    }

    public  enum Type {
        STEPS,
        TRAINING,
        MOVING,
        NOT_SUPPORTED
    }
}