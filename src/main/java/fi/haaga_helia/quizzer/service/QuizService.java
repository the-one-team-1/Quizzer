package fi.haaga_helia.quizzer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.repository.QuizRepository;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

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
}