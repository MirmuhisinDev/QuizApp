package org.example.quizapp.dataloader;

import lombok.RequiredArgsConstructor;
import org.example.quizapp.entity.User;
import org.example.quizapp.entity.enums.Role;
import org.example.quizapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;
    @Override
    public void run(String... args) throws Exception {
        if (ddl.equals("create")) {
            User user = User.builder()
                    .firstName("Super-Admin")
                    .lastName("Admin")
                    .email("superadmin@gmail.com")
                    .password(passwordEncoder.encode("superAdmin"))
                    .role(Role.ROLE_SUPER_ADMIN)
                    .build();
            userRepository.save(user);
        }
    }
}
