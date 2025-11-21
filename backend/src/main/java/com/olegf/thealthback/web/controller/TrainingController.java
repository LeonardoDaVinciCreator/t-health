package com.olegf.thealthback.web.controller;

import com.olegf.thealthback.domain.entity.Training;
import com.olegf.thealthback.domain.service.TrainingService;
import com.olegf.thealthback.utils.Interval;
import com.olegf.thealthback.web.dto.TrainingApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/training")
public class TrainingController {
    private final TrainingService trainingService;

    @PostMapping
    public Training createTraining(@RequestBody TrainingApi.CreateDto createDto) {
        return trainingService.create(createDto);
    }

    @GetMapping("/user/{userId}")
    public List<Training> getUserTrainings(@PathVariable Long userId) {
        return trainingService.getTrainings(userId);
    }

    @GetMapping("/{id}")
    public Training getTrainingById(@PathVariable Long id) {
        return trainingService.getTrainingById(id);
    }

    @PatchMapping("/{id}")
    public Training updateTraining(@PathVariable Long id, @RequestBody TrainingApi.UpdateDto updateDto) {
        return trainingService.update(id, updateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTraining(@PathVariable Long id) {
        trainingService.delete(id);
    }

    @PostMapping("/user/{userId}/stats")
    public List<Training> getUserTrainingStats(
            @PathVariable Long userId,
            @RequestBody Interval interval
    ) {
        return trainingService.getStats(userId, interval);
    }
}
