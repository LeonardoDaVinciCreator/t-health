package com.olegf.thealthback.domain.repository;

import com.olegf.thealthback.domain.entity.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends CrudRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);

    void deleteAllByPostId(Long postId);
}
