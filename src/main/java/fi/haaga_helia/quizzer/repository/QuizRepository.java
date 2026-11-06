package fi.haaga_helia.quizzer.repository;

import fi.haaga_helia.quizzer.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

}