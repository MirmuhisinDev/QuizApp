package org.example.quizapp.payload.request;


import lombok.*;
import org.example.quizapp.entity.User;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionDto {
    private Integer quizId;
    private String question;
    private Long fileId;
    private List<AnswerDto> answerDto;
}
