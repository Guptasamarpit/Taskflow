package com.taskflow.repository;

import com.taskflow.entity.Project;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOwnerIdOrderByIdDesc(Long id);
    List<Project>findAllByOwnerIdAndNameContainingIgnoreCaseOrderByIdDesc(Long ownerId, String name);   
    Optional<Project> findByIdAndOwnerId(Long id, Long ownerId);
}
