package com.olegf.thealthback.web.controller;

import com.olegf.thealthback.domain.entity.Comment;
import com.olegf.thealthback.domain.entity.Post;
import com.olegf.thealthback.domain.service.PostService;
import com.olegf.thealthback.web.dto.CommentCreateDto;
import com.olegf.thealthback.web.dto.PostApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    public Post createPost(@RequestBody PostApi.CreateDto dto) {
        return postService.create(dto);
    }

    @GetMapping
    public List<Post> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return postService.getFeed(page, size);
    }

    @DeleteMapping("/post/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }

    @PostMapping("/{postId}/like")
    public void like(
            @PathVariable Long postId,
            @RequestParam Long userId
    ) {
        postService.like(postId, userId);
    }

    @DeleteMapping("/post/{postId}/like")
    public void unlikePost(@PathVariable Long postId, @RequestParam Long userId) {
        postService.unlike(postId, userId);
    }

    @PostMapping("/comment")
    public void comment(@RequestBody CommentCreateDto dto) {
        postService.comment(dto);
    }

    @GetMapping("/{postId}/comments")
    public List<Comment> getComments(@PathVariable Long postId) {
        return postService.getComments(postId);
    }

    @DeleteMapping("/comment/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        postService.deleteComment(commentId);
    }
}
