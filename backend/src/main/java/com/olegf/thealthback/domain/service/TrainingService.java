package com.olegf.thealthback.domain.service;

import com.olegf.thealthback.domain.entity.Program;
import com.olegf.thealthback.domain.entity.Training;
import com.olegf.thealthback.domain.repository.ProgramRepo;
import com.olegf.thealthback.domain.repository.TrainingRepo;
import com.olegf.thealthback.web.dto.TrainingCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepo trainingRepo;
    private final ProgramRepo programRepo;

    @Transactional(readOnly = true)
    public List<Training> getTrainings(Long userId) {
        return trainingRepo.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Program> getPrograms(Long trainingId) {
        return programRepo.findAllByTrainingId(trainingId);
    }

    public Training create(TrainingCreateDto createDto) {
        var training = trainingRepo.save(new Training(createDto.getUserId()));
        var programs = createDto.getProgram().stream()
                .map(it -> new Program(training.getId(), it.getTitle(), it.getDescription(), it.getMedia(), it.getTiming()))
                .toList();

        programRepo.saveAll(programs);
        return training;
    }
}
