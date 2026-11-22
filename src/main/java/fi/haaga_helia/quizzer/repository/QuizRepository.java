package fi.haaga_helia.quizzer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.haaga_helia.quizzer.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByPublishedTrue();
}