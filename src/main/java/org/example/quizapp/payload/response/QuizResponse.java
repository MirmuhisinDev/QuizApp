package org.example.quizapp.payload.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class QuizResponse {
    private int id;
    private String quizName;
    private int questionCount;
    private int quizTime;
    private String description;
}
