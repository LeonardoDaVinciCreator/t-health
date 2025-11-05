package com.olegf.thealthback.domain.repository;

import com.olegf.thealthback.domain.entity.Training;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepo extends CrudRepository<Training, Long> {
    List<Training> findAllByUserId(Long userId);
}
