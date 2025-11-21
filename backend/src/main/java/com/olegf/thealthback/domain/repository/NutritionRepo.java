package com.olegf.thealthback.domain.repository;

import com.olegf.thealthback.domain.entity.Nutrition;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NutritionRepo extends CrudRepository<Nutrition, Long> {
    List<Nutrition> findAllByUserId(Long userId);

    @Query(
            """
select * from nutrition where user_id = :userId and date >= now() - CAST(:interval as interval);
"""
    )
    List<Nutrition> getStats(Long userId, String interval);
}
