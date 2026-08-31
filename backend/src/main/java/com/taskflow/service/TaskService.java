package com.taskflow.service;

import com.taskflow.dto.TaskDtos.*;
import com.taskflow.entity.*;
import com.taskflow.exception.*;
import com.taskflow.repository.*;
import java.util.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    final TaskRepository ts;
    final ProjectRepository ps;
    final UserRepository us;
    final CommentRepository cs;

    public TaskService(TaskRepository t, ProjectRepository p, UserRepository u, CommentRepository c) {
        ts = t;
        ps = p;
        us = u;
        cs = c; 
    }

    Project project(String e, Long id) {
        User u = us.findByEmail(e).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));
        return ps.findByIdAndOwnerId(id, u.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    TaskResponse d(Task t) {
        return new TaskResponse(t.getId(), t.getTitle(), t.getDescription(), t.getStatus(), t.getPriority(), t.getDueDate());
    }
 @Transactional(readOnly = true)
    public List<TaskResponse> all(String e, Long pid, String status, String priority, String search) {
        project(e, pid);
        List<Task> x;
        if (status != null && !status.isBlank())
            x = ts.findAllByProjectIdAndStatusOrderByIdDesc(pid, TaskStatus.valueOf(status));
        else if (priority != null && !priority.isBlank())
            x = ts.findAllByProjectIdAndPriorityOrderByIdDesc(pid, TaskPriority.valueOf(priority));
        else if (search != null && !search.isBlank())
            x = ts.findAllByProjectIdAndTitleContainingIgnoreCaseOrderByIdDesc(pid, search);
        else
            x = ts.findAllByProjectIdOrderByIdDesc(pid);
        return x.stream().map(this::d).toList();
    }

    public TaskResponse create(String e, Long pid, TaskRequest r) {
        Task t = new Task();
        t.setProject(project(e, pid));
        t.setTitle(r.title());
        t.setDescription(r.description());
        t.setStatus(r.status() == null ? TaskStatus.TODO : r.status());
        t.setPriority(r.priority() == null ? TaskPriority.MEDIUM : r.priority());
        t.setDueDate(r.dueDate());
        return d(ts.save(t));
    }

    public TaskResponse update(String e, Long pid, Long id, TaskRequest r) {
        project(e, pid);
        Task t = ts.findByIdAndProjectId(id, pid)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Task not found"));
        t.setTitle(r.title());
        t.setDescription(r.description());
        t.setStatus(r.status() == null ? TaskStatus.TODO : r.status());
        t.setPriority(r.priority() == null ? TaskPriority.MEDIUM : r.priority());
        t.setDueDate(r.dueDate());
        return d(ts.save(t));
    }
   @Transactional
    public void delete(String e, Long pid, Long id) {
        project(e, pid);
        Task t = ts.findByIdAndProjectId(id, pid)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Task not found"));
                cs.deleteAllByTaskId(id);
        ts.delete(t);
    }
}
