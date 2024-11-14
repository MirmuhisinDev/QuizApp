package org.example.quizapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.quizapp.payload.ApiResponse;
import org.example.quizapp.payload.auth.RequestUser;
import org.example.quizapp.payload.auth.UpdateUser;
import org.example.quizapp.service.authService.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/getOne/{id}")
    public ResponseEntity<ApiResponse> getOne(@PathVariable Integer id) {
        ApiResponse one = userService.getOne(id);
        return ResponseEntity.ok(one);
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size) {
        ApiResponse apiResponse = userService.allUsers(page, size);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Integer id, @RequestBody UpdateUser user) {
        ApiResponse apiResponse = userService.updateUser(user, id);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer id) {
        ApiResponse apiResponse = userService.deleteUser(id);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/save-admin")
    public ResponseEntity<ApiResponse> saveAdmin(@RequestBody @Valid RequestUser user) {
        ApiResponse apiResponse = userService.savedAdmin(user);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchByFirstName(@RequestParam ("firstName")String firstName) {
        ApiResponse apiResponse = userService.searchByFirstName(firstName);
        return ResponseEntity.ok(apiResponse);
    }
}
