package com.olegf.thealthback.web.dto;

import com.olegf.thealthback.domain.entity.Nutrition;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Valid
public sealed class NutrtitionApi permits NutrtitionApi.CreateDto, NutrtitionApi.UpdateDto  {

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static final class CreateDto extends NutrtitionApi {
        private Long userId;
        private String mealName;
        private int mealCalories;
        private Nutrition.MealType mealType;
        private Nutrition.Parameters parameters;
        private LocalDateTime date;
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static final class UpdateDto extends NutrtitionApi {
        private String mealName;
        private int mealCalories;
        private Nutrition.MealType mealType;
        private Nutrition.Parameters parameters;
        private LocalDateTime date;
    }
}
