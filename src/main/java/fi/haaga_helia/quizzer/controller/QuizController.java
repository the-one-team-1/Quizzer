package fi.haaga_helia.quizzer.controller;

import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.service.QuizService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }
}