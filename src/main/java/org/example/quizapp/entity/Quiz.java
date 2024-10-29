package org.example.quizapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String quizName;

    @Column(nullable = false)
    private int questionCount;

    private String quizDescription;

    @Column(nullable = false)
    private int quizTime;

    private boolean deleted;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
