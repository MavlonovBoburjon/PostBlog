package com.example.postblog.service;

import com.example.postblog.model.User;
import com.example.postblog.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final PostService postService;

    public Page<User> page(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (keyword == null || keyword.isBlank()) return userRepo.findAll(pageable);
        return userRepo.findByUsernameContainingIgnoreCase(keyword, pageable);
    }

    public User save(User u) {
        if (u.getPassword() != null) u.setPassword(passwordEncoder.encode(u.getPassword()));
        if (u.getCreatedAt() == null) u.setCreatedAt(java.time.LocalDateTime.now());
        return userRepo.save(u);
    }

    public User get(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void delete(User u) {
        if (u.getPosts() != null) {
            u.getPosts().forEach(postService::delete);
        }
        userRepo.delete(u);
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }
}
