package org.example.quizapp.repository;

import org.example.quizapp.entity.Question;
import org.example.quizapp.entity.Quiz;
import org.example.quizapp.payload.request.QuestionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    boolean existsByQuestionAndDeletedIsFalse(String question);

    List<Question> findAllByQuizIdAndDeletedFalse(Integer quizId);

    boolean existsByQuestionAndIdNot(String question, Integer id);

    Page<Question> findAllByDeletedFalse(Pageable pageable);

    @Query(nativeQuery = true, value = "select * from question where " +
            "quiz_id = :quizId and deleted is false order by random() limit :limit")
    List<Question> findAllByQuizIdAndDeletedIsFalseByLimit(@Param("quizId")  Integer quizId, @Param("limit") Integer limit);

}
