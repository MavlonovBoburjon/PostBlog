package com.example.postblog.service;

import com.example.postblog.model.Post;
import com.example.postblog.model.Picture;
import com.example.postblog.model.User;
import com.example.postblog.repository.PostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepo postRepo;
    private final PictureService pictureService;

    public Page<Post> pageAll(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        if (keyword == null || keyword.isBlank()) return postRepo.findAll(pageable);
        return postRepo.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    public Page<Post> pageForUser(Long userId, int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        if (keyword == null || keyword.isBlank()) return postRepo.findByUserId(userId, pageable);
        return postRepo.findByUserIdAndTitleContainingIgnoreCase(userId, keyword, pageable);
    }

    public Post create(User user, String title, String body, String status, MultipartFile image) throws IOException {
        Picture pic = pictureService.store(image);
        Post p = Post.builder()
                .title(title)
                .body(body)
                .status(status)
                .user(user)
                .picture(pic)
                .build();
        return postRepo.save(p);
    }

    public Post update(Post post, String title, String body, String status, MultipartFile image) throws IOException {
        post.setTitle(title);
        post.setBody(body);
        post.setStatus(status);
        if (image != null && !image.isEmpty()) {
            // delete old
            pictureService.delete(post.getPicture());
            Picture pic = pictureService.store(image);
            post.setPicture(pic);
        }
        return postRepo.save(post);
    }

    public Post get(Long id) {
        return postRepo.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public void delete(Post post) {
        pictureService.delete(post.getPicture());
        postRepo.delete(post);
    }
}
