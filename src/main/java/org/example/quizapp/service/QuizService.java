package org.example.quizapp.service;


import lombok.RequiredArgsConstructor;
import org.example.quizapp.entity.*;
import org.example.quizapp.payload.ApiResponse;
import org.example.quizapp.payload.Pageable;
import org.example.quizapp.payload.request.*;
import org.example.quizapp.payload.response.QuestionResponse;
import org.example.quizapp.payload.response.QuizResponse;
import org.example.quizapp.repository.AnswerRepository;
import org.example.quizapp.repository.QuestionRepository;
import org.example.quizapp.repository.QuizRepository;
import org.example.quizapp.repository.ResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ResultRepository resultRepository;

    public ApiResponse addQuiz(QuizDto quizDto) {
     boolean byQuizName = quizRepository.existsByQuizNameAndDeletedIsFalse(quizDto.getQuizName());

        if (byQuizName) {
            return new ApiResponse("Quiz with name " + quizDto.getQuizName() + " already exists",404);
        }

        Quiz quiz = Quiz.builder()
                .quizName(quizDto.getQuizName())
                .questionCount(quizDto.getQuestionCount())
                .quizTime(quizDto.getQuizTime())
                .quizDescription(quizDto.getDescription())
                .build();
        quizRepository.save(quiz);
        return new ApiResponse("Quiz created successfully",201);
    }

    public ApiResponse getById(int id){
        Optional<Quiz> byId = quizRepository.findById(id);
        if (byId.isPresent()) {
            QuizResponse response = QuizResponse.builder()
                    .id(byId.get().getId())
                    .quizName(byId.get().getQuizName())
                    .questionCount(byId.get().getQuestionCount())
                    .quizTime(byId.get().getQuizTime())
                    .description(byId.get().getQuizDescription())
                    .build();
            return new ApiResponse(response);
        }
        return new ApiResponse("Quiz with id " + id + " not found",404);
    }

    public ApiResponse getAllQuizzes(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Quiz> quizPage = quizRepository.findAllByDeletedFalse(pageRequest);
        List<QuizResponse> responses = new ArrayList<>();
        for (Quiz quiz : quizPage) {
            QuizResponse response = QuizResponse.builder()
                    .id(quiz.getId())
                    .quizName(quiz.getQuizName())
                    .questionCount(quiz.getQuestionCount())
                    .quizTime(quiz.getQuizTime())
                    .description(quiz.getQuizDescription())
                    .build();
            responses.add(response);
        }
        Pageable pageable = Pageable.builder()
                .page(quizPage.getNumber())
                .size(quizPage.getSize())
                .totalPages(quizPage.getTotalPages())
                .totalElements(quizPage.getTotalElements())
                .body(responses)
                .build();
        return new ApiResponse(pageable);
    }

    public ApiResponse updateQuiz(QuizDto quizDto, Integer id) {
        boolean b = quizRepository.existsByQuizNameAndIdNot(quizDto.getQuizName(), id);
        if (!b) {
            Quiz quiz = Quiz.builder()
                    .quizName(quizDto.getQuizName())
                    .questionCount(quizDto.getQuestionCount())
                    .quizTime(quizDto.getQuizTime())
                    .quizDescription(quizDto.getDescription())
                    .build();
            quizRepository.save(quiz);
            return new ApiResponse("Quiz updated successfully",200);
        }
        return new ApiResponse("Quiz with id " + id + " not found",404);
    }

    public ApiResponse deleteQuiz(int id){
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            return new ApiResponse("Quiz with id " + id + " not found",404);
        }

        List<Question> allByQuiz = questionRepository.findAllByQuizIdAndDeletedFalse(id);
        List<Question> questions = new ArrayList<>();

        for (Question question : allByQuiz) {
            question.setQuiz(null);
            questions.add(question);
        }

        Quiz quiz1 = quiz.get();
        quiz1.setDeleted(true);
        questionRepository.saveAll(questions);
        quizRepository.save(quiz1);
        return new ApiResponse("Quiz with id " + id + " deleted successfully",204);
    }

    public ApiResponse startQuiz(Integer quizId){
        Optional<Quiz> byId = quizRepository.findById(quizId);
        if (byId.isEmpty()) {
            return new ApiResponse("Quiz with id " + quizId + " not found",404);
        }

        List<Question> allByQuizId = questionRepository.findAllByQuizIdAndDeletedIsFalseByLimit(quizId, byId.get().getQuestionCount());

        List<QuestionResponse> responses = new ArrayList<>();

        for (Question question : allByQuizId) {
            List<Answer> allByQuestionIdAndDeletedFalse = answerRepository.findAllByQuestionIdAndDeletedFalse(question.getId());
            List<AnswerDto> answerDto = new ArrayList<>();
            for (Answer answer : allByQuestionIdAndDeletedFalse) {
                AnswerDto answer1 = answerDto(answer);
                answerDto.add(answer1);
            }
            QuestionResponse response = QuestionResponse.builder()
                    .id(question.getId())
                    .quizId(quizId)
                    .question(question.getQuestion())
                    .fileId(null)
                    .answerDto(answerDto)
                    .build();
            responses.add(response);
        }
        StartQuizDto quizDto = StartQuizDto.builder()
                .id(quizId)
                .quizName(byId.get().getQuizName())
                .questions(responses)
                .quizTime(byId.get().getQuizTime())
                .questionCount(byId.get().getQuestionCount())
                .build();
        return new ApiResponse(quizDto);
    }

    public AnswerDto answerDto(Answer answer) {
        return AnswerDto.builder()
                .answer(answer.getAnswer())
                .correct(answer.isCorrect())
                .build();
    }

    public ApiResponse passQuiz(Integer quizId, List<PassQuestionDto> questionId, User user, Integer duration) {
        Optional<Quiz> byId = quizRepository.findById(quizId);
        if (byId.isEmpty()) {
            return new ApiResponse("Quiz with id " + quizId + " not found",404);
        }
        Quiz quiz = byId.get();

        int correctAnswer = 0;

        for (PassQuestionDto passQuestionDto : questionId) {

            Optional<Question> byId1 = questionRepository.findById(passQuestionDto.getQuestionId());
            if (byId1.isEmpty()) {
                return new ApiResponse("Question with id " + passQuestionDto.getQuestionId() + " not found",404);
            }

            Answer answerTrue = answerRepository.findByQuestionIdAndIsCorrectTrue(passQuestionDto.getQuestionId());
            if (answerTrue.getId().equals(passQuestionDto.getAnswerID())){
                correctAnswer++;
            }
        }

        Result result = Result.builder()
                .user(user)
                .quiz(quiz)
                .questionCount(quiz.getQuestionCount())
                .correctAnswer(correctAnswer)
                .resultTime(duration)
                .build();
        resultRepository.save(result);
        return new ApiResponse("Test finished successfully"+ result.getId(),200);
    }
}