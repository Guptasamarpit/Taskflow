package com.taskflow.service;

import com.taskflow.dto.UserDtos.*;
import com.taskflow.entity.User;
import com.taskflow.exception.*;
import com.taskflow.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.stereotype.*;

@Service
public class UserService {
    final UserRepository users;

    public UserService(UserRepository u) {
        users = u;
    }

    User user(String email) {
        return users.findByEmail(email).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    public UserResponse me(String e) {
        User u = user(e);
        return new UserResponse(u.getId(), u.getName(), u.getEmail());
    }

    public UserResponse update(String e, UpdateProfileRequest r) {
        User u = user(e);
        if (!u.getEmail().equalsIgnoreCase(r.email()) && users.existsByEmail(r.email().toLowerCase()))
            throw new ApiException(HttpStatus.CONFLICT, "Email already registered");
        u.setName(r.name());
        u.setEmail(r.email().toLowerCase());
        users.save(u);
        return new UserResponse(u.getId(), u.getName(), u.getEmail());
    }
}
