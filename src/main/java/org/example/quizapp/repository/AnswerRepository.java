package org.example.quizapp.repository;

import org.example.quizapp.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findAllByQuestionIdAndDeletedFalse(Integer questionId);

    @Query(nativeQuery = true, value = "select * from answer where question_id=?1 and is_correct is true")
    Answer findByQuestionIdAndIsCorrectTrue(Integer questionId);
}
