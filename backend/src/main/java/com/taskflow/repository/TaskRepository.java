package com.taskflow.repository;

import com.taskflow.entity.Task;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByProjectIdOrderByIdDesc(Long id);

    Optional<Task> findByIdAndProjectId(Long id, Long projectId);

    void deleteAllByProjectId(Long id);
}
