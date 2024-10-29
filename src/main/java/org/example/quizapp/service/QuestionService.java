package org.example.quizapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.quizapp.entity.Answer;
import org.example.quizapp.entity.Question;
import org.example.quizapp.entity.Quiz;
import org.example.quizapp.entity.User;
import org.example.quizapp.payload.ApiResponse;
import org.example.quizapp.payload.Pageable;
import org.example.quizapp.payload.request.AnswerDto;
import org.example.quizapp.payload.request.QuestionDto;
import org.example.quizapp.payload.response.QuestionResponse;
import org.example.quizapp.repository.AnswerRepository;
import org.example.quizapp.repository.FileRepository;
import org.example.quizapp.repository.QuestionRepository;
import org.example.quizapp.repository.QuizRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final FileRepository fileRepository;
    private final AnswerRepository answerRepository;


    @Transactional
    public ApiResponse addQuestion(QuestionDto questionDto, User user) {
        boolean b = questionRepository.existsByQuestionAndDeletedIsFalse(questionDto.getQuestion());
        if (b) {
            return new ApiResponse("Question already exists", 400);
        }

        Optional<Quiz> byId = quizRepository.findById(questionDto.getQuizId());

        if (byId.isEmpty()) {
            return new ApiResponse("Quiz not found", 404);
        }

        Question question = Question.builder()
                .quiz(byId.get())
                .question(questionDto.getQuestion())
                .file(questionDto.getFileId() != null ? fileRepository.findById(questionDto.getFileId()).get() : null)
                .createdBy(user)
                .build();
        Question save = questionRepository.save(question);

        for (AnswerDto answerDto : questionDto.getAnswerDto()) {
            Answer answer = answer(answerDto, save.getId());
            answerRepository.save(answer);
        }

        return new ApiResponse("Question successfully added", 201);
    }

    public ApiResponse getById(int id) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isEmpty()) {
            return new ApiResponse("Question not found", 404);
        }

        List<Answer> allByQuestionId = answerRepository.findAllByQuestionIdAndDeletedFalse(id);
        List<AnswerDto> answerDtoList = new ArrayList<>();
        for (Answer answer : allByQuestionId) {
            AnswerDto answerDto = answerDto(answer);
            answerDtoList.add(answerDto);
        }

            QuestionResponse questionResponse = QuestionResponse.builder()
                    .id(question.get().getId())
                    .quizId(question.get().getQuiz().getId())
                    .question(question.get().getQuestion())
                    .fileId(question.get().getFile().getId())
                    .answerDto(answerDtoList)
                    .createdAt(question.get().getCreatedAt())
                    .build();
            return new ApiResponse(questionResponse);
    }

    public ApiResponse getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Question> questions = questionRepository.findAllByDeletedFalse(pageRequest);
        List<QuestionResponse> responses = new ArrayList<>();



        for (Question question : questions) {
            QuestionResponse response = QuestionResponse.builder()
                    .id(question.getId())
                    .quizId(question.getQuiz().getId())
                    .question(question.getQuestion())
                    .ownerFirstName(question.getCreatedBy() != null ? question.getCreatedBy().getFirstName() : null)
                    .ownerLastName(question.getCreatedBy() != null ? question.getCreatedBy().getLastName() : null)
                    .createdAt(question.getCreatedAt())
                    .fileId(question.getFile() != null ? question.getFile().getId() : null)
                    .build();
            responses.add(response);
        }
        Pageable pageable = Pageable.builder()
                .page(questions.getNumber())
                .size(questions.getSize())
                .totalPages(questions.getTotalPages())
                .totalElements(questions.getTotalElements())
                .body(responses)
                .build();
        return new ApiResponse(pageable);
    }

    public ApiResponse updateQuestion(QuestionDto questionDto, Integer id) {
        Optional<Question> byId2 = questionRepository.findById(id);
        if (byId2.isEmpty()) {
            return new ApiResponse("Question not found", 404);
        }

        Optional<Quiz> byId1 = quizRepository.findById(questionDto.getQuizId());
        if (byId1.isEmpty()) {
            return new ApiResponse("Quiz not found", 404);
        }

        boolean byId = questionRepository.existsByQuestionAndIdNot(questionDto.getQuestion(), id);
        if (byId) {
            return new ApiResponse("Question already exists", 400);
        }

        Question question = byId2.get();
        question.setQuiz(byId1.get());
        question.setQuestion(questionDto.getQuestion());
        question.setFile(questionDto.getFileId() != null ? fileRepository.findById(questionDto.getFileId()).get() : null);
        Question save = questionRepository.save(question);

        for (AnswerDto answerDto : questionDto.getAnswerDto()) {
            Answer answer = answer(answerDto, save.getId());
            answerRepository.save(answer);
        }

        return new ApiResponse("Question id not found", 404);
    }

    public ApiResponse deleteById(int id) {
        Optional<Question> byId = questionRepository.findById(id);
        if (byId.isEmpty()) {
            return new ApiResponse("Question not found", 404);
        }

        List<Answer> allByQuestionId = answerRepository.findAllByQuestionIdAndDeletedFalse(id);
        List<Answer> answerList = new ArrayList<>();

        for (Answer answer : allByQuestionId) {
            answer.setQuestion(null);
            answer.setDeleted(true);
            answerList.add(answer);
        }

        Question question = byId.get();
        question.setDeleted(true);
        answerRepository.saveAll(answerList);
        questionRepository.save(question);
        return new ApiResponse("Question successfully deleted", 200);
    }

    public Answer answer(AnswerDto answerDto, Integer id) {
        Optional<Question> byId = questionRepository.findById(id);
        return Answer.builder()
                .question(byId.get())
                .answer(answerDto.getAnswer())
                .isCorrect(answerDto.isCorrect())
                .build();
    }

    public AnswerDto answerDto(Answer answer) {
        return AnswerDto.builder()
                .answer(answer.getAnswer())
                .correct(answer.isCorrect())
                .build();
    }
}
