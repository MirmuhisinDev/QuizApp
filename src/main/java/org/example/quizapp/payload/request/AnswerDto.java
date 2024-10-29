package org.example.quizapp.payload.request;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AnswerDto {
    private String answer;
    private boolean correct;

}
