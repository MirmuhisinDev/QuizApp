package org.example.quizapp.payload.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizDto {
    @NotBlank
    private String quizName;
    private Integer questionCount;
    private String description;
    private Integer quizTime;
}
