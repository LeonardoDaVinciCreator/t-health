package com.olegf.thealthback.domain.repository;

import com.olegf.thealthback.domain.entity.Training;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepo extends CrudRepository<Training, Long> {
    List<Training> findAllByUserId(Long userId);

    @Query("""
    select * from training where user_id = :userId
    left join exercises on exercises.training_id = training.id
    where exercices.title = :exerciseTitle
""")
    List<Training> findByExerciseTitle(Long userId, String exerciseTitle);
}
