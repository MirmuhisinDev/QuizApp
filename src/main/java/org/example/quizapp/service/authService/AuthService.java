package org.example.quizapp.service.authService;

import lombok.RequiredArgsConstructor;
import org.example.quizapp.entity.User;
import org.example.quizapp.entity.enums.Role;
import org.example.quizapp.payload.ApiResponse;
import org.example.quizapp.payload.auth.RequestLogin;
import org.example.quizapp.payload.auth.RequestUser;
import org.example.quizapp.repository.UserRepository;
import org.example.quizapp.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Component
@RequiredArgsConstructor

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final JwtProvider jwtProvider;

    public ApiResponse register(RequestUser requestUser){
        Optional<User> byEmail = userRepository.findByEmail(requestUser.getEmail());
        if(byEmail.isPresent()){
            return new ApiResponse("Email already exists", 400);
        }
        int randomNumber = randomNumber();
        User user = User.builder()
                .firstName(requestUser.getFirstName())
                .lastName(requestUser.getLastName())
                .birthDate(requestUser.getBirthDate())
                .email(requestUser.getEmail())
                .password(passwordEncoder.encode(requestUser.getPassword()))
                .role(Role.ROLE_TESTER)
                .activationCode(randomNumber)
                .enabled(false)
                .build();
        userRepository.save(user);
        emailSenderService.sendEmail(user.getEmail(), "VERIFY EMAIL", "Your Activation Code: "+ randomNumber);
        return new ApiResponse("Successfully registered", 201);
    }
    public int randomNumber(){
        Random random = new Random();
        return random.nextInt(90000)+10000;
    }

    public ApiResponse login(RequestLogin requestUser){
        Optional<User> byEmail = userRepository.findByEmail(requestUser.getEmail());
        if(byEmail.isEmpty()){
            return new ApiResponse("Your login not found", 404);
        }
        User user = byEmail.get();
        if (!user.isEnabled()) return new ApiResponse("Your account is disabled", 403);
        boolean matches = passwordEncoder.matches(requestUser.getPassword(), user.getPassword());
        if (matches){
            String s = jwtProvider.generateToken(user.getEmail());
            return new ApiResponse(s+": :"+user.getRole(),200);
        }
        return new ApiResponse("Your password don't match", 400);
    }

    public ApiResponse activation(int activationCode){
        Optional<User> byActivationCode = userRepository.findByActivationCode(activationCode);
        if(byActivationCode.isPresent()){
            User user = byActivationCode.get();
            user.setEnabled(true);
            user.setActivationCode(null);
            userRepository.save(user);
            return new ApiResponse("Successfully activated", 200);
        }
        return new ApiResponse("Activation code not found", 404);
    }

    public ApiResponse saveAdmin(RequestUser requestUser){
        Optional<User> byEmail = userRepository.findByEmail(requestUser.getEmail());
        if(byEmail.isPresent()){
            return new ApiResponse("Email already exists", 400);
        }
        User user = User.builder()
                .firstName(requestUser.getFirstName())
                .lastName(requestUser.getLastName())
                .birthDate(requestUser.getBirthDate())
                .email(requestUser.getEmail())
                .password(passwordEncoder.encode(requestUser.getPassword()))
                .role(Role.ROLE_ADMIN)
                .enabled(false)
                .build();
        userRepository.save(user);
        return new ApiResponse("Successfully saved admin", 201);
    }
}
