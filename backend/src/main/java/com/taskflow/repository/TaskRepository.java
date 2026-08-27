package com.taskflow.repository;

import com.taskflow.entity.Task;
import com.taskflow.entity.TaskPriority;
import com.taskflow.entity.TaskStatus;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByProjectIdOrderByIdDesc(Long projectId);

    List<Task> findAllByProjectIdAndStatusOrderByIdDesc(Long projectId, TaskStatus status);

    List<Task> findAllByProjectIdAndPriorityOrderByIdDesc(Long projectId, TaskPriority priority);

    List<Task> findAllByProjectIdAndTitleContainingIgnoreCaseOrderByIdDesc(Long projectId, String title);

    long countByProjectOwnerId(Long userId);

    long countByProjectOwnerIdAndStatus(Long userId, TaskStatus status);

    long countByProjectOwnerIdAndPriority(Long userId, TaskPriority priority);

    Optional<Task> findByIdAndProjectId(Long id, Long projectId);

    void deleteAllByProjectId(Long id);
}
