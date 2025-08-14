package com.example.postblog;

import com.example.postblog.model.Role;
import com.example.postblog.model.User;
import com.example.postblog.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class PostBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostBlogApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepo userRepo, BCryptPasswordEncoder encoder) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                userRepo.save(User.builder()
                        .username("admin")
                        .password(encoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .createdAt(LocalDateTime.now())
                        .build());
            }
            if (userRepo.findByUsername("user").isEmpty()) {
                userRepo.save(User.builder()
                        .username("user")
                        .password(encoder.encode("user123"))
                        .role(Role.USER)
                        .createdAt(LocalDateTime.now())
                        .build());
            }
        };
    }
}
