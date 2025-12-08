package com.olegf.thealthback.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("post_likes")
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    private Long id;
    private long postId;
    private long userId;
    private LocalDateTime createdAt;

    public static Like from(Long postId, Long userId) {
        return new Like(
                null,
                postId,
                userId,
                LocalDateTime.now()
        );
    }
}
