package com.taskflow.service;

import com.taskflow.dto.CommentDtos.*;
import com.taskflow.entity.*;
import com.taskflow.exception.*;
import com.taskflow.repository.*;
import java.util.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    final CommentRepository cs;
    final TaskRepository ts;
    final ProjectRepository ps;
    final UserRepository us;

    public CommentService(CommentRepository c, TaskRepository t, ProjectRepository p, UserRepository u) {
        cs = c;
        ts = t;
        ps = p;
        us = u;
    }

    User user(String e) {
        return us.findByEmail(e).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    Task task(String e, Long pid, Long tid) {
        User u = user(e);
        Project p = ps.findByIdAndOwnerId(pid, u.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Project not found"));
        return ts.findByIdAndProjectId(tid, p.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    CommentResponse d(Comment c) {
        return new CommentResponse(c.getId(), c.getContent(), c.getCreatedAt(), c.getAuthor().getId(),
                c.getAuthor().getName());
    }

 @Transactional(readOnly = true)
public List<CommentResponse> all(String e, Long pid, Long tid) {
    task(e, pid, tid);
    return cs.findAllByTaskIdOrderByCreatedAtAsc(tid)
            .stream()
            .map(this::d)
            .toList();
}

    public CommentResponse add(String e, Long pid, Long tid, CommentRequest r) {
        Comment c = new Comment();
        c.setTask(task(e, pid, tid));
        c.setAuthor(user(e));
        c.setContent(r.content());
        return d(cs.save(c));
    }

    public void delete(String e, Long pid, Long tid, Long cid) {
        task(e, pid, tid);
        Comment c = cs.findByIdAndTaskId(cid, tid)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Comment not found"));
        if (!c.getAuthor().getEmail().equalsIgnoreCase(e))
            throw new ApiException(HttpStatus.FORBIDDEN, "You can only delete your own comment");
        cs.delete(c);
    }
}
