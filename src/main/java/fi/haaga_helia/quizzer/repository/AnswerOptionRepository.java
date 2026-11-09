package fi.haaga_helia.quizzer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.haaga_helia.quizzer.model.AnswerOption;

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
    List<AnswerOption> findByQuestionId(Long questionId);
}