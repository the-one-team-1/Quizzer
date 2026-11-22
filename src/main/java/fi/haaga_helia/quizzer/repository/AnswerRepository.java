package fi.haaga_helia.quizzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.haaga_helia.quizzer.model.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    long countByQuestionId(Long questionId);

    long countByQuestionIdAndCorrectTrue(Long questionId);
}
