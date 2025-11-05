package com.olegf.thealthback.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("program")
@AllArgsConstructor
public class Program {
    @Id
    private Long id;
    private Long trainingId;
    private String title;
    private String description;
    private byte[] media;
    private Timing timing;

    public Program(Long trainingId, String title, String description, byte[] media, Timing timing) {
        this.trainingId = trainingId;
        this.title = title;
        this.description = description;
        this.media = media;
        this.timing = timing;
    }

    public static class Timing {
        private int amount;
        private Unit unit;

        public enum Unit {
            SECOND,
            MINUTE,
            HOUR
        }
    }
}
