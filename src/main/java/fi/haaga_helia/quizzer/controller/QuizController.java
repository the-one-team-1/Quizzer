package fi.haaga_helia.quizzer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haaga_helia.quizzer.dto.ApiDto.QuestionDto;
import fi.haaga_helia.quizzer.dto.ApiDto.QuestionResultDto;
import fi.haaga_helia.quizzer.dto.ApiDto.QuizDto;
import fi.haaga_helia.quizzer.service.QuizService;

// Student REST API for quizzes (published quizzes only)
@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/published")
    public List<QuizDto> getPublishedQuizzes() {
        return quizService.getPublishedQuizzes();
    }

    // GET /api/quizzes -> all published quizzes, with or without a category
    @GetMapping
    public List<QuizDto> getQuizzes() {
        return quizService.getPublishedQuizzes();
    }

    // GET /api/quizzes/{id}
    @GetMapping("/{id}")
    public QuizDto getQuiz(@PathVariable Long id) {
        return quizService.getPublishedQuiz(id);
    }

    // GET /api/quizzes/{id}/questions -> questions with answer options (no correct flag)
    @GetMapping("/{id}/questions")
    public List<QuestionDto> getQuestions(@PathVariable Long id) {
        return quizService.getQuestions(id);
    }

    // GET /api/quizzes/{id}/results -> answer counts per question
    @GetMapping("/{id}/results")
    public List<QuestionResultDto> getResults(@PathVariable Long id) {
        return quizService.getResults(id);
    }
}
