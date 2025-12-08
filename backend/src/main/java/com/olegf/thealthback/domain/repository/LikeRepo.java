package com.olegf.thealthback.domain.repository;

import com.olegf.thealthback.domain.entity.Like;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@ReadingConverter
public interface LikeRepo extends CrudRepository<Like, Long> {
    @Query("select * from post_likes where post_id = :postId and user_id = :userId")
    Optional<Like> findByPostAndUser(Long postId, Long userId);

    void deleteAllByPostId(long postId);
}
