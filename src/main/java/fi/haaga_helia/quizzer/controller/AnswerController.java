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

// Student REST API: submit an answer
@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private final AnswerRepository answerRepository;
    private final AnswerOptionRepository answerOptionRepository;

    public AnswerController(AnswerRepository answerRepository,
            AnswerOptionRepository answerOptionRepository) {
        this.answerRepository = answerRepository;
        this.answerOptionRepository = answerOptionRepository;
    }

    // POST /api/answers  { "answerOptionId": 5 } -> { correct, correctOptionIds }
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
