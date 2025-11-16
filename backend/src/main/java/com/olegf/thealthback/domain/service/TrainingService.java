package com.olegf.thealthback.domain.service;

import com.olegf.thealthback.domain.EntityNotFoundException;
import com.olegf.thealthback.domain.entity.Training;
import com.olegf.thealthback.domain.repository.TrainingRepo;
import com.olegf.thealthback.web.dto.TrainingApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepo trainingRepo;

    public Training create(TrainingApi.CreateDto createDto) {
        return trainingRepo.save(Training.from(createDto));
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

    public Training update(Long id, TrainingApi.UpdateDto updateDto) {
        var existing = getTrainingById(id);
        existing.doUpdate(updateDto);

        return trainingRepo.save(existing);
    }

    public void delete(Long id) {
        if(!trainingRepo.existsById(id)) throw new EntityNotFoundException("Training not found with id " + id);
        trainingRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Training> searchByCriteria(Long userId, TrainingApi.SearchCriteria criteria) {
        var trainings = trainingRepo.findAllByUserId(userId);

        return trainings;
    }

    @Transactional(readOnly = true)
    public List<Training> getStats(Long userId, TrainingApi.Interval interval) {
        return trainingRepo.getStats(userId, interval.valueString());
    }
}
