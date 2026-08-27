package com.taskflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 2000)
    private String content;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Task task;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User author;

    @PrePersist
    void created() {
        createdAt = Instant.now();
    }
}
