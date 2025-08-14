package com.example.postblog.controller;

import com.example.postblog.model.Role;
import com.example.postblog.model.User;
import com.example.postblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public String list(@RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value="size", defaultValue="10") int size,
                       @RequestParam(value="q", required=false) String q,
                       Model model) {
        Page<User> users = userService.page(page, size, q);
        model.addAttribute("usersPage", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("q", q == null ? "" : q);
        return "users";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        User u = new User();
        u.setRole(Role.USER);
        model.addAttribute("user", u);
        model.addAttribute("roles", Role.values());
        return "user_form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.get(id));
        model.addAttribute("roles", Role.values());
        return "user_form";
    }

    @PostMapping("/save")
    public String save(@RequestParam(required=false) Long id,
                       @RequestParam String username,
                       @RequestParam(required=false) String password,
                       @RequestParam Role role) {

        if (id == null) {
            User u = User.builder().username(username).password(password).role(role).build();
            userService.save(u);
        } else {
            User existing = userService.get(id);
            existing.setRole(role);
            existing.setUsername(username);
            if (password != null && !password.isBlank()) {
                existing.setPassword(password);
            }
            userService.save(existing);
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        User u = userService.get(id);
        userService.delete(u);
        return "redirect:/admin/users";
    }
}
