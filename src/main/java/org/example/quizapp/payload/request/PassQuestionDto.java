package org.example.quizapp.payload.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class PassQuestionDto {

    private Integer questionId;
    private Integer answerID;
}
