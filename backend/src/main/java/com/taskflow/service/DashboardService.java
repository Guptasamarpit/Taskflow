package com.taskflow.service;

import com.taskflow.dto.DashboardDtos.*;
import com.taskflow.entity.TaskStatus;
import com.taskflow.entity.TaskPriority;
import com.taskflow.repository.*;
import org.springframework.stereotype.*;

@Service
public class DashboardService {
    final ProjectRepository ps;
    final TaskRepository ts;
    final UserRepository us;

    public DashboardService(ProjectRepository p, TaskRepository t, UserRepository u) {
        ps = p;
        ts = t;
        us = u;
    }

    public DashboardResponse summary(String e) {
        Long id = us.findByEmail(e).orElseThrow().getId();
        return new DashboardResponse(ps.findAllByOwnerIdOrderByIdDesc(id).size(), ts.countByProjectOwnerId(id),
                ts.countByProjectOwnerIdAndStatus(id, TaskStatus.TODO),
                ts.countByProjectOwnerIdAndStatus(id, TaskStatus.IN_PROGRESS),
                ts.countByProjectOwnerIdAndStatus(id, TaskStatus.DONE),
                ts.countByProjectOwnerIdAndPriority(id, TaskPriority.HIGH));
    }
}
