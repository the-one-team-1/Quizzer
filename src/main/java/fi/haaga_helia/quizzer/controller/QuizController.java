package fi.haaga_helia.quizzer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.service.QuizService;


@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/published")
    public List<Quiz> getPublishedQuizzes() {
        return quizService.getPublishedQuizzes();
    }
}