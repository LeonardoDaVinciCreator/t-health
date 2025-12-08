package com.olegf.thealthback.domain.repository;

import com.olegf.thealthback.domain.entity.Post;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends CrudRepository<Post, Long> {
    @Query("select * from posts order by created_at desc limit :limit offset :offset")
    List<Post> getFeed(int limit, int offset);

    List<Post> findByUserId(Long userId);
}
