package com.olegf.thealthback.web.controller;

import com.olegf.thealthback.domain.entity.Nutrition;
import com.olegf.thealthback.domain.service.NutritionService;
import com.olegf.thealthback.utils.Interval;
import com.olegf.thealthback.web.dto.NutrtitionApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/nutrition")
public class NutritionController {
    private final NutritionService nutritionService;

    @PostMapping
    public Nutrition createNutrition(@RequestBody NutrtitionApi.CreateDto createDto) {
        return nutritionService.create(createDto);
    }

    @GetMapping("/user/{userId}")
    public List<Nutrition> getUserNutritions(@PathVariable Long userId) {
        return nutritionService.getNutritions(userId);
    }

    @GetMapping("/{id}")
    public Nutrition getNutritionById(@PathVariable Long id) {
        return nutritionService.getNutritionById(id);
    }

    @PatchMapping("/{id}")
    public Nutrition updateNutrition(@PathVariable Long id, @RequestBody NutrtitionApi.UpdateDto updateDto) {
        return nutritionService.update(id, updateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteNutrition(@PathVariable Long id) {
        nutritionService.delete(id);
    }

    @PostMapping("/user/{userId}/stats")
    public List<Nutrition> getUserNutritionStats(
            @PathVariable Long userId,
            @RequestBody Interval interval
    ) {
        return nutritionService.getStats(userId, interval);
    }
}
