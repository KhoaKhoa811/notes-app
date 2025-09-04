package com.example.notes_app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.ErrorResponse;

import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
