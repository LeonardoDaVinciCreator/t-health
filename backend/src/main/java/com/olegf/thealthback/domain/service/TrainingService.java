package com.olegf.thealthback.domain.service;

import com.olegf.thealthback.domain.EntityNotFoundException;
import com.olegf.thealthback.domain.entity.Exercise;
import com.olegf.thealthback.domain.entity.Training;
import com.olegf.thealthback.domain.repository.ProgramRepo;
import com.olegf.thealthback.domain.repository.TrainingRepo;
import com.olegf.thealthback.web.dto.TrainingCreateDto;
import com.olegf.thealthback.web.dto.TrainingSearchCriteria;
import com.olegf.thealthback.web.dto.TrainingStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepo trainingRepo;
    private final ProgramRepo programRepo;

    public Training create(TrainingCreateDto createDto) {
        var training = trainingRepo.save(new Training(createDto.getUserId()));
        var programs = createDto.getProgram().stream()
                .map(it -> new Exercise(training.getId(), it.getTitle(), it.getDescription(), it.getMedia(), it.getTiming()))
                .toList();

        programRepo.saveAll(programs);
        return training;
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainings(Long userId) {
        return trainingRepo.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Training getTrainingById(Long id) {
        return trainingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training not found"));
    }

    public Training update(Long id, TrainingCreateDto createDto) {
        var existing = trainingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training not found"));

        var updated = new Training(
                existing.getId(),
                existing.getUserId(),
                createDto.getStartDate(),
                createDto.getProgram().stream()
                        .map(it -> new Exercise(existing.getId(), it.getTitle(), it.getDescription(), it.getMedia(), it.getTiming()))
                        .toList()
        );

        programRepo.deleteAll(programRepo.findAllByTrainingId(existing.getId()));
        programRepo.saveAll(updated.getExercise());

        return trainingRepo.save(updated);
    }

    public void delete(Long id) {
        if(!trainingRepo.existsById(id)) throw new EntityNotFoundException("Training not found with id " + id);

        programRepo.findAllByTrainingId(id)
                .forEach(p -> programRepo.deleteById(p.getId()));
        trainingRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public TrainingStatsDto getStats(Long userId, LocalDateTime start, LocalDateTime end) {
        var trainings = trainingRepo.findAllByUserId(userId);
        var filtered = trainings.stream()
                .filter(t -> {
                    if (start == null || end == null) return true;
                    return  t.getStartDate().isAfter(start) && t.getStartDate().isBefore(end);
                })
                .toList();

        var totalPrograms = filtered.stream()
                .mapToInt(t -> t.getExercise().size())
                .sum();

        return new TrainingStatsDto(filtered.size(), totalPrograms);
    }

    @Transactional(readOnly = true)
    public List<Exercise> getPrograms(Long trainingId) {
        return programRepo.findAllByTrainingId(trainingId);
    }

    public List<Training> getByCriteria(Long userId, TrainingSearchCriteria criteria) {
        return trainingRepo.findByExerciseTitle(userId, criteria.getExerciseTitle());
    }
}
