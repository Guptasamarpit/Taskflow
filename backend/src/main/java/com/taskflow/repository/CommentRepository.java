package com.taskflow.repository;

import com.taskflow.entity.Comment;
import java.util.*; 
import org.springframework.data.jpa.repository.JpaRepository;
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTaskIdOrderByIdDesc(Long taskId);
    
    Optional<Comment> findByIdAndTaskId(Long id, Long taskId);

    void deleteAllByTaskId(Long taskId);

    void deleteAllByTaskProjectId(Long projectId);
}
