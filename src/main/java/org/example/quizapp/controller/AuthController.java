package org.example.quizapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.quizapp.payload.ApiResponse;
import org.example.quizapp.payload.auth.RequestLogin;
import org.example.quizapp.payload.auth.RequestUser;
import org.example.quizapp.service.authService.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor

public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid RequestUser user) {
        ApiResponse register = authService.register(user);
        return ResponseEntity.status(register.getStatus()).body(register);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody RequestLogin user) {
        ApiResponse login = authService.login(user);
        return ResponseEntity.ok(login);
    }

    @PutMapping("/code")
    public ResponseEntity<ApiResponse> activeCode(@RequestParam int code){
        ApiResponse activation = authService.activation(code);
        return ResponseEntity.status(activation.getStatus()).body(activation);
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/save-admin")
    public ResponseEntity<ApiResponse> saveAdmin(@RequestBody @Valid RequestUser user) {
        ApiResponse saveAdmin = authService.saveAdmin(user);
        return ResponseEntity.status(saveAdmin.getStatus()).body(saveAdmin);
    }
}
