package org.example.quizapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Quiz quiz;

    @ManyToOne
    private File file;

    @Column(nullable = false, columnDefinition = "text")
    private String question;

    @ManyToOne
    @CreatedBy
    private User createdBy;

    private boolean deleted;
    @CreationTimestamp
    private LocalDateTime createdAt;

}
