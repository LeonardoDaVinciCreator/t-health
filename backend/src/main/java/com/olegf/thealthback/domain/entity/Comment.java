package com.olegf.thealthback.domain.entity;

import com.olegf.thealthback.web.dto.CommentCreateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("post_comments")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    private Long id;
    private Long postId;
    private Long authorId;
    private String authorName;
    private String text;
    private LocalDateTime createdAt;

    public static Comment from(CommentCreateDto dto, String authorName) {
        return new Comment(
                null,
                dto.getPostId(),
                dto.getAuthorId(),
                authorName,
                dto.getText(),
                LocalDateTime.now()
        );
    }
}
