package com.taskflow.repository;

import com.taskflow.entity.*;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByProjectIdOrderByIdDesc(Long projectId);

    List<Task>findAllByProjectIdAndStatusOrderByIdDesc(Long projectId, TaskStatus status);

    List<Task> findAllByProjectIdAndPriorityOrderByIdDesc(Long projectId, TaskPriority priority);

    List<Task> findAllByProjectIdAndTitleContainingIgnoreCaseOrderByIdDesc(Long projectId, String title);

    long countByProjectOwnerId(Long ownerId);

    long countByProjectOwnerIdAndStatus(Long ownerId, TaskStatus status);

    long countByProjectOwnerIdAndPriority(Long ownerId, TaskPriority priority);

    Optional<Task> findByIdAndProjectId(Long id, Long projectId);

    void deleteAllByProjectId(Long id);
}
