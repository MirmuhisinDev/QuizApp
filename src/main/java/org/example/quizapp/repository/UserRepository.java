package org.example.quizapp.repository;

import org.example.quizapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByActivationCode(Integer activationCode);

    @Query(nativeQuery = true, value = "select * from users where first_name ilike '%' || :firstName || '%'")
    List<User> searchByFirstName(String firstName);
}
