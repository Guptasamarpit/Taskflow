package com.taskflow.controller;

import com.taskflow.dto.UserDtos.*;
import com.taskflow.service.UserService;
import jakarta.validation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
public class UserController {
    final UserService s;

    public UserController(UserService s) {
        this.s = s;
    }

    @GetMapping
    UserResponse me(Authentication a) {
        return s.me(a.getName());
    }

    @PutMapping
    UserResponse update(Authentication a, @Valid @RequestBody UpdateProfileRequest r) {
        return s.update(a.getName(), r);
    }
}
