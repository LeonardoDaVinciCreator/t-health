package com.olegf.thealthback.domain.entity;

import com.olegf.thealthback.web.dto.ActivityCreateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Table("activities")
@AllArgsConstructor
public class Activity {
    @Id
    private Long id;
    private long userId;
    private int steps;
    private int value;
    private List<String> goals;
    private double calories;
    private LocalDateTime date;

    public static Activity from(ActivityCreateDto createDto) {
       return new Activity(
              null,
               createDto.getUserId(),
               createDto.getSteps(),
               createDto.getValue(),
               createDto.getGoals(),
               createDto.getCalories(),
               LocalDateTime.now()
        );
    }
}
