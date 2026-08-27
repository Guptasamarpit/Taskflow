package com.taskflow.controller;

import com.taskflow.dto.ProjectDtos.*;
import com.taskflow.dto.TaskDtos.*;
import com.taskflow.dto.CommentDtos.*;
import com.taskflow.service.*;
import jakarta.validation.*;
import java.util.*;
import org.springframework.security.core.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    final ProjectService p;
    final TaskService t;
    final CommentService c;

    public ProjectController(ProjectService p, TaskService t, CommentService c) {
        this.p = p;
        this.t = t;
        this.c = c;
    }

    @GetMapping
    List<ProjectResponse> all(Authentication a,@RequestParam(required = false) String search) {
        return p.all(a.getName(), search);
    }

    @GetMapping("/{id}")
    ProjectResponse get(Authentication a, @PathVariable Long id) {
        return p.get(a.getName(), id);
    }

    @PostMapping
    ProjectResponse create(Authentication a, @Valid @RequestBody ProjectRequest r) {
        return p.create(a.getName(), r);
    }

    @PutMapping("/{id}")
    ProjectResponse update(Authentication a, @PathVariable Long id, @Valid @RequestBody ProjectRequest r) {
        return p.update(a.getName(), id, r);
    }

    @DeleteMapping("/{id}")
    void delete(Authentication a, @PathVariable Long id) {
        p.delete(a.getName(), id);
    }

    @GetMapping("/{pid}/tasks")
    List<TaskResponse> tasks(Authentication a, @PathVariable Long pid,@RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,@RequestParam(required = false) String search) {
        return t.all(a.getName(), pid,status,priority,search);
    }

    @PostMapping("/{pid}/tasks")
    TaskResponse add(Authentication a, @PathVariable Long pid, @Valid @RequestBody TaskRequest r) {
        return t.create(a.getName(), pid, r);
    }

    @PutMapping("/{pid}/tasks/{id}")
    TaskResponse update(Authentication a, @PathVariable Long pid, @PathVariable Long id,
            @Valid @RequestBody TaskRequest r) {
        return t.update(a.getName(), pid, id, r);
    }

    @DeleteMapping("/{pid}/tasks/{id}")
    void deleteTask(Authentication a, @PathVariable Long pid, @PathVariable Long id) {
        t.delete(a.getName(), pid, id);
    }
        @GetMapping("/{pid}/tasks/{tid}/comments")
    List<CommentResponse> comments(Authentication a, @PathVariable Long pid, @PathVariable Long tid) {
        return c.all(a.getName(), pid, tid);
    }

    @PostMapping("/{pid}/tasks/{tid}/comments")
    CommentResponse addComment(Authentication a, @PathVariable Long pid, @PathVariable Long tid,
            @Valid @RequestBody CommentRequest r) {
        return c.add(a.getName(), pid, tid, r);
    }

    @DeleteMapping("/{pid}/tasks/{tid}/comments/{cid}")
    void deleteComment(Authentication a, @PathVariable Long pid, @PathVariable Long tid, @PathVariable Long cid) {
        c.delete(a.getName(), pid, tid, cid);
    }
}
