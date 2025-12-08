package com.olegf.thealthback.domain.entity;

import com.olegf.thealthback.web.dto.PostApi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("posts")
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String mediaUrl;
    private LocalDateTime createdAt;
    private int likesCount;
    private int commentsCount;

    public static Post from(PostApi.CreateDto dto) {
        return new Post(
                null,
                dto.getUserId(),
                dto.getTitle(),
                dto.getContent(),
                dto.getMediaUrl(),
                LocalDateTime.now(),
                0,
                0
        );
    }
}
