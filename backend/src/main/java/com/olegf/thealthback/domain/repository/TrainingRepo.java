package com.olegf.thealthback.domain.repository;

import com.olegf.thealthback.domain.entity.Training;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingRepo extends CrudRepository<Training, Long> {
    List<Training> findAllByUserId(Long userId);

    @Query("""
select * from training t
where t.user_id = :userId
and t.date >= :dateFrom
""")
    List<Training> getStats(Long userId, LocalDateTime dateFrom);
}
