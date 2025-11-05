package com.olegf.thealthback.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@Table("training")
@AllArgsConstructor
public class Training {
    @Id
    private Long id;
    private long userId;
    private LocalDateTime startDate;
    private List<Program> program;

    public Training(Long userId) {
        this.userId = userId;
        this.startDate = LocalDateTime.now();
        this.program = Collections.emptyList();
    }
}
