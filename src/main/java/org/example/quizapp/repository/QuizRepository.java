package org.example.quizapp.repository;

import org.example.quizapp.entity.Quiz;
import org.example.quizapp.payload.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    boolean existsByQuizNameAndDeletedIsFalse(String quizName);

    boolean existsByQuizNameAndIdNot(String quizName, Integer id);

    boolean existsByQuizName(String quizName);

    Page<Quiz> findAllByDeletedFalse(PageRequest pageRequest);




}
