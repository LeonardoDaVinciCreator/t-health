package com.olegf.thealthback.domain.repository;

import com.olegf.thealthback.domain.entity.Exercise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramRepo extends CrudRepository<Exercise, Long> {
    List<Exercise> findAllByTrainingId(Long trainingId);
}
