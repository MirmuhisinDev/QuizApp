package org.example.quizapp.payload;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class ApiResponse {
    private String message;
    private int status;
    private Object body;

    public ApiResponse(Object body) {
        this.body = body;
    }

    public ApiResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
