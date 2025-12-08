package com.olegf.thealthback.domain.service;

import com.olegf.thealthback.domain.EntityNotFoundException;
import com.olegf.thealthback.domain.entity.Comment;
import com.olegf.thealthback.domain.entity.Like;
import com.olegf.thealthback.domain.entity.Post;
import com.olegf.thealthback.domain.repository.CommentRepo;
import com.olegf.thealthback.domain.repository.LikeRepo;
import com.olegf.thealthback.domain.repository.PostRepo;
import com.olegf.thealthback.domain.repository.UserRepo;
import com.olegf.thealthback.web.dto.CommentCreateDto;
import com.olegf.thealthback.web.dto.PostApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepo postRepo;
    private final CommentRepo commentRepo;
    private final LikeRepo likeRepo;
    private final UserRepo userRepo;

    public Post create(PostApi.CreateDto dto) {
        return postRepo.save(Post.from(dto));
    }

    @Transactional
    public List<Post> getFeed(int page, int size) {
        int offset = page * size;
        return postRepo.getFeed(size, offset);
    }

    @Transactional
    public void deletePost(Long postId) {
        var post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id " + postId));

        commentRepo.deleteAllByPostId(postId);
        likeRepo.deleteAllByPostId(postId);

        postRepo.delete(post);
    }

    @Transactional
    public void like(Long postId, Long userId) {
        if (likeRepo.findByPostAndUser(postId, userId).isPresent()) {
            return;
        }

        likeRepo.save(Like.from(postId, userId));

        postRepo.findById(postId).ifPresent(p -> {
            p.setLikesCount(p.getLikesCount() + 1);
            postRepo.save(p);
        });
    }

    @Transactional
    public void unlike(Long postId, Long userId) {
        var like = likeRepo.findByPostAndUser(postId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Like not found with id " + postId));

        likeRepo.delete(like);

        postRepo.findById(postId).ifPresent(p -> {
            p.setLikesCount(Math.max(0, p.getLikesCount() - 1));
            postRepo.save(p);
        });
    }

    @Transactional
    public void comment(CommentCreateDto dto) {
        var author = userRepo.findById(dto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("User not found" + dto.getAuthorId()));

        commentRepo.save(Comment.from(dto, author.getUsername()));

        postRepo.findById(dto.getPostId()).ifPresent(post -> {
            post.setCommentsCount(post.getCommentsCount() + 1);
            postRepo.save(post);
        });
    }

    @Transactional(readOnly = true)
    public List<Comment> getComments(Long postId) {
        return commentRepo.findAllByPostId(postId);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        var comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id " + commentId));

        postRepo.findById(comment.getPostId()).ifPresent(post -> {
            post.setCommentsCount(post.getCommentsCount() - 1);
            postRepo.save(post);
        });

        commentRepo.delete(comment);
    }
}
