package com.olegf.thealthback.domain.service;

import com.olegf.thealthback.domain.EntityNotFoundException;
import com.olegf.thealthback.domain.entity.Nutrition;
import com.olegf.thealthback.domain.repository.NutritionRepo;
import com.olegf.thealthback.utils.Interval;
import com.olegf.thealthback.web.dto.NutrtitionApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NutritionService {
    private final NutritionRepo nutritionRepo;

    public Nutrition create(NutrtitionApi.CreateDto createDto) {
        return nutritionRepo.save(Nutrition.from(createDto));
    }

    @Transactional(readOnly = true)
    public List<Nutrition> getNutritions(Long userId) {
        return nutritionRepo.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Nutrition getNutritionById(Long id) {
        return nutritionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nutrition not found with id: " + id));
    }

    public Nutrition update(Long id, NutrtitionApi.UpdateDto updateDto) {
        var existing = getNutritionById(id);
        existing.doUpdate(updateDto);

        return nutritionRepo.save(existing);
    }

    public void delete(Long id) {
        if(!nutritionRepo.existsById(id)) throw new EntityNotFoundException("Training not found with id " + id);
        nutritionRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Nutrition> getStats(Long userId, Interval interval) {
        return nutritionRepo.getStats(userId, interval.valueString());
    }
}
