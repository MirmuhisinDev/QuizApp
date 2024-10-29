package org.example.quizapp.payload.request;


import lombok.*;
import org.example.quizapp.payload.response.QuestionResponse;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class StartQuizDto {
    private int id;
    private String quizName;
    private List<QuestionResponse> questions;
    private int quizTime;
    private int questionCount;
}
