package com.redline.blog.services;

import java.util.UUID;

import com.redline.blog.domain.entities.User;

public interface UserService {
    User getUserById(UUID userId);
}
