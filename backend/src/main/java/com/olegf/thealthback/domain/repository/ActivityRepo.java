package com.olegf.thealthback.domain.repository;

import com.olegf.thealthback.domain.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepo extends CrudRepository<Activity, Long> {
    Page<Activity> findByUserId(Long userId, Pageable pageable);

//    @Query("""
//    select * from activities where user_id = :userId and date in ()
//""")
    Page<Activity> findByDate(Long userId, LocalDateTime date, Pageable pageable);
}
