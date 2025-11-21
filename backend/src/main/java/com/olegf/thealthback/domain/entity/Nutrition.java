package com.olegf.thealthback.domain.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olegf.thealthback.web.dto.NutrtitionApi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.time.LocalDateTime;

@Data
@Table("nutrition")
@AllArgsConstructor
@NoArgsConstructor
public class Nutrition {
    @Id
    private long id;
    @Column("user_id")
    private Long userId;
    private String mealName;
    private int mealCalories;
    private MealType mealType;
    @Column("parameters")
    private Parameters parameters;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    private static final ObjectMapper mapper = new ObjectMapper();

    public Nutrition(Long userId, String mealName, MealType mealType, LocalDateTime date, Parameters parameters, int mealCalories) {
        this.userId = userId;
        this.mealName = mealName;
        this.mealType = mealType;
        this.date = date;
        this.mealCalories = mealCalories;
        this.parameters = parameters;
    }

    public enum MealType {
        BREAKFAST,
        LUNCH,
        DINNER
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Parameters {
        private double protein;
        private double fats;
        private double carbohydrate;
    }

    public void doUpdate(NutrtitionApi.UpdateDto updateDto) {
        this.mealName = updateDto.getMealName();
        this.mealType = updateDto.getMealType();
        this.mealCalories = updateDto.getMealCalories();
        this.parameters = updateDto.getParameters();
    }

    public static Nutrition from(NutrtitionApi.CreateDto createDto) {
        return new Nutrition(
                createDto.getUserId(),
                createDto.getMealName(),
                createDto.getMealType(),
                createDto.getDate(),
                createDto.getParameters(),
                createDto.getMealCalories()
        );
    }
}

