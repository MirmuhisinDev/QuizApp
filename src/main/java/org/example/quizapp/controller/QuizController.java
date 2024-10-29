package org.example.quizapp.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.quizapp.payload.ApiResponse;
import org.example.quizapp.payload.request.QuizDto;
import org.example.quizapp.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor

public class QuizController {
    private final QuizService quizService;

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createQuiz(@RequestBody @Valid QuizDto quizDto) {
        ApiResponse apiResponse = quizService.addQuiz(quizDto);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<ApiResponse> getOne(@PathVariable int id) {
        ApiResponse byId = quizService.getById(id);
        return ResponseEntity.ok(byId);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size) {
        ApiResponse allQuizzes = quizService.getAllQuizzes(page, size);
        return ResponseEntity.ok(allQuizzes);
    }
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateQuiz(@PathVariable int id, @RequestBody QuizDto quizDto) {
        ApiResponse apiResponse = quizService.updateQuiz(quizDto, id);
        return ResponseEntity.ok(apiResponse);
    }
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteQuiz(@PathVariable int id) {
        ApiResponse apiResponse = quizService.deleteQuiz(id);
        return ResponseEntity.ok(apiResponse);
    }
}
