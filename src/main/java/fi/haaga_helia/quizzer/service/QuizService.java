package fi.haaga_helia.quizzer.service;

import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.repository.QuizRepository;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz addQuiz(Quiz quiz) {
    return quizRepository.save(quiz);
}
}