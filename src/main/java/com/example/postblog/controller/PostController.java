package com.example.postblog.controller;

import com.example.postblog.model.Post;
import com.example.postblog.model.Role;
import com.example.postblog.model.User;
import com.example.postblog.service.PostService;
import com.example.postblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    public String list(
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value="size", defaultValue="5") int size,
            @RequestParam(value="q", required=false) String q,
            Authentication auth,
            Model model) {

        User me = userService.findByUsername(auth.getName());

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<Post> posts = postService.pageAll(page, size, q, me.getId(), isAdmin);

        model.addAttribute("postsPage", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("q", q);
        model.addAttribute("uploadPath", uploadPath);
        model.addAttribute("me", me);

        return "posts";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("post", new Post());
        return "post_form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Authentication auth, Model model) {
        Post p = postService.get(id);
        User me = userService.findByUsername(auth.getName());
        if (me.getRole() != Role.ADMIN && !p.getUser().getId().equals(me.getId())) throw new RuntimeException("Not allowed");
        model.addAttribute("post", p);
        return "post_form";
    }

    @PostMapping("/save")
    public String save(@RequestParam(required=false) Long id,
                       @RequestParam String title,
                       @RequestParam String body,
                       @RequestParam(defaultValue="PUBLISHED") String status,
                       @RequestParam(required=false) MultipartFile image,
                       Authentication auth) throws Exception {

        User me = userService.findByUsername(auth.getName());
        if (id == null) {
            postService.create(me, title, body, status, image);
        } else {
            Post existing = postService.get(id);
            if (me.getRole() != Role.ADMIN && !existing.getUser().getId().equals(me.getId())) throw new RuntimeException("Not allowed");
            postService.update(existing, title, body, status, image);
        }
        return "redirect:/posts";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Authentication auth) {
        Post p = postService.get(id);
        User me = userService.findByUsername(auth.getName());
        if (me.getRole() != Role.ADMIN && !p.getUser().getId().equals(me.getId())) throw new RuntimeException("Not allowed");
        postService.delete(p);
        return "redirect:/posts";
    }
}
