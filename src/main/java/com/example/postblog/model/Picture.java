package com.example.postblog.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pictures")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Picture {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String fileName;

    private String originalName;
    private String contentType;
    private String filePath;

    private LocalDateTime createdAt = LocalDateTime.now();
}
