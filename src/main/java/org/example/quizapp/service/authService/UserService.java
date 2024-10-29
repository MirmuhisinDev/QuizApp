package org.example.quizapp.service.authService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.example.quizapp.entity.User;
import org.example.quizapp.entity.enums.Role;
import org.example.quizapp.payload.ApiResponse;
import org.example.quizapp.payload.Pageable;
import org.example.quizapp.payload.auth.RequestUser;
import org.example.quizapp.payload.auth.UpdateUser;
import org.example.quizapp.payload.response.UserResponse;
import org.example.quizapp.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse getOne(Integer id){
        Optional<User> byId = userRepository.findById(id);
        if(byId.isPresent()){
            UserResponse response = UserResponse.builder()
                    .firstName(byId.get().getFirstName())
                    .lastName(byId.get().getLastName())
                    .birthDate(byId.get().getBirthDate())
                    .email(byId.get().getEmail())
                    .password(byId.get().getPassword())
                    .build();
            return new ApiResponse(response);
        }
        return new ApiResponse("user not found", 404);
    }

    public ApiResponse allUsers(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageRequest);
        List<UserResponse> responses = new ArrayList<>();
        for (User user : users) {
            UserResponse response = UserResponse.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .birthDate(user.getBirthDate())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .build();
            responses.add(response);
        }
        Pageable pageable = Pageable.builder()
                .page(users.getNumber())
                .size(users.getSize())
                .totalPages(users.getTotalPages())
                .totalElements(users.getTotalElements())
                .body(responses)
                .build();
        return new ApiResponse(pageable);
    }

    public ApiResponse updateUser(UpdateUser updateUser, Integer id){
        Optional<User> byId = userRepository.findById(id);
        if(byId.isPresent()){
            User user = byId.get();
            user.setFirstName(updateUser.getFirstName());
            user.setLastName(updateUser.getLastName());
            user.setBirthDate(updateUser.getBirthDate());
            user.setEmail(updateUser.getEmail());
            userRepository.save(user);
            return new ApiResponse("User updated successfully",200);
        }
        return new ApiResponse("User not found", 404);
    }

    public ApiResponse deleteUser(Integer id){
        Optional<User> byId = userRepository.findById(id);
        if(byId.isPresent()){
            User user = byId.get();
            user.setDeleted(true);
            userRepository.save(user);
        }
        return new ApiResponse("User not found", 404);
    }

    public ApiResponse searchByFirstName(String firstName){
        List<User> users = userRepository.searchByFirstName(firstName);
        List<UserResponse> responses = new ArrayList<>();
        for (User user : users) {
            UserResponse response = UserResponse.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .birthDate(user.getBirthDate())
                    .email(user.getEmail())
                    .build();
            responses.add(response);
            return new ApiResponse(responses);
        }
        return new ApiResponse("User not found", 404);
    }

    public ApiResponse savedAdmin(RequestUser requestUser){
        Optional<User> byEmail = userRepository.findByEmail(requestUser.getEmail());
        if(byEmail.isPresent()){
            return new ApiResponse("User already exists", 400);
        }
        User user = User.builder()
                .firstName(requestUser.getFirstName())
                .lastName(requestUser.getLastName())
                .birthDate(requestUser.getBirthDate())
                .email(requestUser.getEmail())
                .password(passwordEncoder.encode(requestUser.getPassword()))
                .role(Role.ROLE_ADMIN)
                .build();
        userRepository.save(user);
        return new ApiResponse("Admin created successfully", 201);
    }
}
