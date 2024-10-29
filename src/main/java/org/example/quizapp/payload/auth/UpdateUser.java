package org.example.quizapp.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class UpdateUser {
    private String firstName;
    private String lastName;
    private int birthDate;
    private String email;
}
