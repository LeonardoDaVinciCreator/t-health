package com.olegf.thealthback.web.dto;

import lombok.Data;

@Data
public class CommentCreateDto {
    private Long postId;
    private Long authorId;
    private String text;
}
