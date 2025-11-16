package com.olegf.thealthback.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Table("nutrition")
@AllArgsConstructor
public class Nutrition {
    @Id
    private long id;
    @Column("user_id")
    private Long userId;
    private String mealName;
    private int mealCalories;
    private MealType mealType;
    private PfcType pfcType;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    public enum MealType {
        BREAKFAST,
        LUNCH,
        DINNER
    }

    public enum PfcType {
        PROTEIN,
        FATS,
        CARBOHYDRATE
    }
}
