package org.example.quizapp.repository;

import org.example.quizapp.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<Result> findByQuizId(Integer id);
}
