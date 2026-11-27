package fi.haaga_helia.quizzer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haaga_helia.quizzer.dto.ApiDto.AnswerRequest;
import fi.haaga_helia.quizzer.dto.ApiDto.AnswerResponse;
import fi.haaga_helia.quizzer.model.Answer;
import fi.haaga_helia.quizzer.model.AnswerOption;
import fi.haaga_helia.quizzer.model.Question;
import fi.haaga_helia.quizzer.repository.AnswerOptionRepository;
import fi.haaga_helia.quizzer.repository.AnswerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

// Student REST API: submit an answer
@RestController
@RequestMapping("/api/answers")
@Tag(name = "Answers", description = "Submitting students' answers")
public class AnswerController {

    private final AnswerRepository answerRepository;
    private final AnswerOptionRepository answerOptionRepository;

    public AnswerController(AnswerRepository answerRepository,
            AnswerOptionRepository answerOptionRepository) {
        this.answerRepository = answerRepository;
        this.answerOptionRepository = answerOptionRepository;
    }

    // POST /api/answers  { "answerOptionId": 5 } -> { correct, correctOptionIds }
    @Operation(summary = "Submit an answer",
            description = "Stores the answer option that the student chose for a question. "
                    + "The request body is { \"answerOptionId\": 1 }. "
                    + "The response tells whether the answer was correct and which options are correct.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Answer saved successfully"),
            @ApiResponse(responseCode = "400", description = "Answer option id is not provided"),
            @ApiResponse(responseCode = "404", description = "Answer option with the provided id does not exist, or its quiz is not published")
    })
    @PostMapping
    public AnswerResponse submitAnswer(@RequestBody AnswerRequest request) {
        if (request == null || request.answerOptionId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "answerOptionId is required");
        }

        AnswerOption option = answerOptionRepository.findById(request.answerOptionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Answer option not found"));

        Question question = option.getQuestion();
        if (question == null || question.getQuiz() == null || !question.getQuiz().isPublished()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer option not found");
        }

        answerRepository.save(new Answer(question, option, option.isCorrect()));

        List<Long> correctOptionIds = answerOptionRepository.findByQuestionId(question.getId())
                .stream()
                .filter(AnswerOption::isCorrect)
                .map(AnswerOption::getId)
                .toList();

        return new AnswerResponse(option.isCorrect(), correctOptionIds);
    }
}
