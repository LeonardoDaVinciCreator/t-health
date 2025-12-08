package com.olegf.thealthback.web.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Valid
public sealed class PostApi permits PostApi.CreateDto, PostApi.Response {
    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class CreateDto extends PostApi {
        private Long userId;
        private String title;
        private String content;
        private String mediaUrl;
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static final class Response extends PostApi {
        private Long id;
        private Long userId;
        private String title;
        private String content;
        private String mediaUrl;
        private LocalDateTime createdAt;
        private int likes;
        private int comments;
    }
}
