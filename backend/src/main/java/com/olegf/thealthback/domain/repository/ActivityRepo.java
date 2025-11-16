package com.olegf.thealthback.domain.repository;

import com.olegf.thealthback.domain.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepo extends CrudRepository<Activity, Long> {
    List<Activity> findByUserId(Long userId);
    @Query("""
    SELECT * FROM activities
    WHERE user_id = :userId
      AND date >= :startOfDay
      AND date < :endOfDay
    ORDER BY date DESC
""")
    List<Activity> findByDate(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
