package org.example.quizapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.quizapp.entity.User;
import org.example.quizapp.payload.ApiResponse;
import org.example.quizapp.payload.request.QuestionDto;
import org.example.quizapp.security.CurrentUser;
import org.example.quizapp.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor

public class QuestionController {

    private final QuestionService questionService;

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(@RequestBody QuestionDto questionDto,
                                              @CurrentUser User user) {
        ApiResponse apiResponse = questionService.addQuestion(questionDto, user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<ApiResponse> getOne(@PathVariable Integer id) {
        ApiResponse byId = questionService.getById(id);
        return ResponseEntity.ok(byId);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5")int size) {
        ApiResponse all = questionService.getAll(page, size);
        return ResponseEntity.ok(all);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Integer id, @RequestBody QuestionDto questionDto) {
        ApiResponse apiResponse = questionService.updateQuestion(questionDto, id);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Integer id) {
        ApiResponse apiResponse = questionService.deleteById(id);
        return ResponseEntity.ok(apiResponse);
    }
}
