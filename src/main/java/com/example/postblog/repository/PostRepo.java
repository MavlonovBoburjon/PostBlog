package com.example.postblog.repository;

import com.example.postblog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContainingIgnoreCase(String username, Pageable pageable);
    Page<Post> findByUserIdAndTitleContainingIgnoreCase(Long userId, String username, Pageable pageable);
    Page<Post> findByUserId(Long userId, Pageable pageable);
}
