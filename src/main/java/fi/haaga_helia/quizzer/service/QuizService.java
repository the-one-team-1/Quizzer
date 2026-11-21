package fi.haaga_helia.quizzer.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import fi.haaga_helia.quizzer.dto.ApiDto.CategoryDto;
import fi.haaga_helia.quizzer.dto.ApiDto.OptionDto;
import fi.haaga_helia.quizzer.dto.ApiDto.QuestionDto;
import fi.haaga_helia.quizzer.dto.ApiDto.QuestionResultDto;
import fi.haaga_helia.quizzer.dto.ApiDto.QuizDto;
import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.repository.AnswerOptionRepository;
import fi.haaga_helia.quizzer.repository.AnswerRepository;
import fi.haaga_helia.quizzer.repository.QuestionRepository;
import fi.haaga_helia.quizzer.repository.QuizRepository;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final AnswerRepository answerRepository;

    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository,
            AnswerOptionRepository answerOptionRepository, AnswerRepository answerRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
        this.answerRepository = answerRepository;
    }

    // ----- Used by the teacher pages -----

    public Quiz addQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found: " + id));
    }

    // ----- Used by the student REST API (only published quizzes) -----

    public List<QuizDto> getPublishedQuizzes() {
        return quizRepository.findByPublishedTrue().stream().map(this::toDto).toList();
    }

    public List<QuizDto> getPublishedQuizzesByCategory(Long categoryId) {
        return quizRepository.findByCategoryIdAndPublishedTrue(categoryId).stream()
                .map(this::toDto).toList();
    }

    public QuizDto getPublishedQuiz(Long id) {
        return toDto(findPublished(id));
    }

    public List<QuestionDto> getQuestions(Long quizId) {
        findPublished(quizId);
        return questionRepository.findByQuizId(quizId).stream()
                .map(question -> new QuestionDto(
                        question.getId(),
                        question.getQuestionText(),
                        question.getDifficulty(),
                        answerOptionRepository.findByQuestionId(question.getId()).stream()
                                .map(option -> new OptionDto(option.getId(), option.getOptionText()))
                                .toList()))
                .toList();
    }

    public List<QuestionResultDto> getResults(Long quizId) {
        findPublished(quizId);
        return questionRepository.findByQuizId(quizId).stream()
                .map(question -> {
                    long total = answerRepository.countByQuestionId(question.getId());
                    long correct = answerRepository.countByQuestionIdAndCorrectTrue(question.getId());
                    return new QuestionResultDto(
                            question.getId(),
                            question.getQuestionText(),
                            question.getDifficulty(),
                            total,
                            correct,
                            total - correct);
                })
                .toList();
    }

    private Quiz findPublished(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));
        if (!quiz.isPublished()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }
        return quiz;
    }

    private QuizDto toDto(Quiz quiz) {
        CategoryDto category = null;
        if (quiz.getCategory() != null) {
            category = new CategoryDto(quiz.getCategory().getId(), quiz.getCategory().getName(),
                    quiz.getCategory().getDescription());
        }
        return new QuizDto(quiz.getId(), quiz.getName(), quiz.getDescription(), quiz.getCourseCode(),
                quiz.isPublished(), quiz.getCreatedAt(), category,
                questionRepository.countByQuizId(quiz.getId()));
    }
}
