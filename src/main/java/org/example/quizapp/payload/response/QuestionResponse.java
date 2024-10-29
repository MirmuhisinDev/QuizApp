package org.example.quizapp.payload.response;


import lombok.*;
import org.example.quizapp.entity.User;
import org.example.quizapp.payload.request.AnswerDto;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionResponse {
    private int id;
    private Integer quizId;
    private String question;
    private Long fileId;
    private List<AnswerDto> answerDto;
    private LocalDateTime createdAt;
    private String ownerFirstName;
    private String ownerLastName;
}
