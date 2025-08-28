package com.redline.blog.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.redline.blog.domain.entities.User;
import com.redline.blog.repositories.UserRepository;
import com.redline.blog.services.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    

}
