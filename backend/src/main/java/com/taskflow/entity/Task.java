package com.taskflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

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
    @Column(length = 2000)
    String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TaskStatus status = TaskStatus.TODO;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TaskPriority priority = TaskPriority.MEDIUM;
    LocalDate dueDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    Project project;
}
