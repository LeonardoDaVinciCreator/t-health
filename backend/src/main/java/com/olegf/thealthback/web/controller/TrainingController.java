package com.olegf.thealthback.web.controller;

import com.olegf.thealthback.domain.entity.Training;
import com.olegf.thealthback.domain.service.TrainingService;
import com.olegf.thealthback.web.dto.TrainingApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/training")
public class TrainingController {
    private final TrainingService trainingService;

    @PostMapping
    public Training createTraining(@RequestBody TrainingCreateDto createDto) {
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

    @PutMapping("/{id}")
    public Training updateTraining(@PathVariable Long id, @RequestBody TrainingCreateDto updateDto) {
        return trainingService.update(id, updateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTraining(@PathVariable Long id) {
        trainingService.delete(id);
    }

    @GetMapping("/user/{userId}/stats")
    public TrainingStatsDto getUserTrainingStats(
            @PathVariable Long userId,
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) LocalDateTime end
    ) {
        return trainingService.getStats(userId, start, end);
    }
}
