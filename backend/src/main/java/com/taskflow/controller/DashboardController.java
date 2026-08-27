package com.taskflow.controller;

import com.taskflow.dto.DashboardDtos.*;
import com.taskflow.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    final DashboardService s;

    public DashboardController(DashboardService s) {
        this.s = s;
    }

    @GetMapping("/summary")
    DashboardResponse summary(Authentication a) {
        return s.summary(a.getName());
    }
}
