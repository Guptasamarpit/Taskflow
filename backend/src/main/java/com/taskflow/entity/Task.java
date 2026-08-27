package com.taskflow.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String title;
    String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TaskStatus status = TaskStatus.TODO;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.MEDIUM;
    private LocalDate dueDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    Project project;
}
