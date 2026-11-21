package fi.haaga_helia.quizzer.dto;

import java.time.LocalDateTime;
import java.util.List;

// JSON shapes sent to and from the student front end.
// Answer options never include the "correct" flag, so students can't read the answers.
public class ApiDto {

    public record CategoryDto(Long id, String name, String description) {}

    public record QuizDto(Long id, String name, String description, String courseCode,
            boolean published, LocalDateTime createdAt, CategoryDto category, long questionCount) {}

    public record OptionDto(Long id, String optionText) {}

    public record QuestionDto(Long id, String questionText, String difficulty,
            List<OptionDto> answerOptions) {}

    public record AnswerRequest(Long answerOptionId) {}

    public record AnswerResponse(boolean correct, List<Long> correctOptionIds) {}

    public record QuestionResultDto(Long questionId, String questionText, String questionDifficulty,
            long totalAnswers, long correctAnswers, long wrongAnswers) {}
}
