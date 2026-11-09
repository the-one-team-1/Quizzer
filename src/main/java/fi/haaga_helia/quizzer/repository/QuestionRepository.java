package fi.haaga_helia.quizzer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.haaga_helia.quizzer.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuizId(Long quizId);
}