package com.taskflow.service;

import com.taskflow.dto.ProjectDtos.*;
import com.taskflow.entity.*;
import com.taskflow.event.producer.TaskFlowEventProducer;
import com.taskflow.exception.*;
import com.taskflow.repository.*;
import java.util.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {
    final ProjectRepository ps;
    final UserRepository us;
    final TaskRepository ts;
    final CommentRepository cs;
    final TaskFlowEventProducer eventProducer;

    public ProjectService(ProjectRepository p, UserRepository u, TaskRepository t, CommentRepository c,
            TaskFlowEventProducer eventProducer) {
        ps = p;
        us = u;
        ts = t;
        cs = c;
        this.eventProducer = eventProducer;
    }

    User user(String e) {
        return us.findByEmail(e).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    Project p(String e, Long id) {
        return ps.findByIdAndOwnerId(id, user(e).getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    ProjectResponse d(Project p) {
        return new ProjectResponse(p.getId(), p.getName(), p.getDescription(),p.getStatus());
    }

      public List<ProjectResponse> all(String e, String search) {
        Long uid = user(e).getId();
        List<Project> x = search == null || search.isBlank() ? ps.findAllByOwnerIdOrderByIdDesc(uid)
                : ps.findAllByOwnerIdAndNameContainingIgnoreCaseOrderByIdDesc(uid, search);
        return x.stream().map(this::d).toList();
    }

    public ProjectResponse get(String e, Long id) {
        return d(p(e, id));
    }

    public ProjectResponse create(String e, ProjectRequest r) {
        Project p = new Project();
        p.setName(r.name());
        p.setDescription(r.description());
        p.setStatus(r.status() == null ? ProjectStatus.ACTIVE : r.status());
        p.setOwner(user(e));
        Project saved = ps.save(p);
        eventProducer.publish("ProjectCreated", saved.getOwner().getId(), saved.getId(), null,
                Map.of("name", saved.getName(), "description", saved.getDescription(), "status",
                        saved.getStatus().name()));
        return d(saved);
    }

    public ProjectResponse update(String e, Long id, ProjectRequest r) {
        Project p = p(e, id);
        p.setName(r.name());
        p.setDescription(r.description());
        p.setStatus(r.status() == null ? ProjectStatus.ACTIVE : r.status());
        Project saved = ps.save(p);
        eventProducer.publish("ProjectUpdated", saved.getOwner().getId(), saved.getId(), null,
                Map.of("name", saved.getName(), "description", saved.getDescription(), "status",
                        saved.getStatus().name()));
        return d(saved);
    }
    
    @Transactional
    public void delete(String e, Long id) {
        Project p = p(e, id);
        cs.deleteAllByTaskProjectId(id);
        ts.deleteAllByProjectId(id);
        ps.delete(p);
        eventProducer.publish("ProjectDeleted", p.getOwner().getId(), p.getId(), null,
                Map.of("projectId", p.getId(), "name", p.getName()));
    }
}
