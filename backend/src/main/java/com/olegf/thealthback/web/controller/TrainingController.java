package com.olegf.thealthback.web.controller;

import com.olegf.thealthback.domain.entity.Training;
import com.olegf.thealthback.domain.service.TrainingService;
import com.olegf.thealthback.web.dto.TrainingCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/training")
public class TrainingController {
    private final TrainingService trainingService;

    @PostMapping
    public Training createTraining(@RequestBody TrainingCreateDto createDto) {
        return trainingService.create(createDto);
    }
}
