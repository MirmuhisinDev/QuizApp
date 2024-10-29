package org.example.quizapp.payload.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private int birthDate;
    private String email;
    private String password;
}
