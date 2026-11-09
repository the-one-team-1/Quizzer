package fi.haaga_helia.quizzer.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fi.haaga_helia.quizzer.model.AnswerOption;
import fi.haaga_helia.quizzer.model.Question;
import fi.haaga_helia.quizzer.repository.AnswerOptionRepository;
import fi.haaga_helia.quizzer.repository.QuestionRepository;
import fi.haaga_helia.quizzer.repository.QuizRepository;

@Controller
public class QuestionPageController {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final AnswerOptionRepository answerOptionRepository;

    public QuestionPageController(QuestionRepository questionRepository,
                                  QuizRepository quizRepository,
                                  AnswerOptionRepository answerOptionRepository) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
        this.answerOptionRepository = answerOptionRepository;
    }

    // Story 6: list the questions of a quiz
    @GetMapping("/quizzes/{quizId}/questions")
    public String listQuestions(@PathVariable Long quizId, Model model) {
        model.addAttribute("quiz", quizRepository.findById(quizId).orElseThrow());
        model.addAttribute("questions", questionRepository.findByQuizId(quizId));
        return "questions";
    }

    @GetMapping("/quizzes/{quizId}/questions/new")
    public String showAddQuestionPage(@PathVariable Long quizId, Model model) {
        model.addAttribute("quiz", quizRepository.findById(quizId).orElseThrow());
        model.addAttribute("question", new Question());
        return "add-question";
    }

    @PostMapping("/quizzes/{quizId}/questions")
    public String addQuestion(@PathVariable Long quizId,
                              @ModelAttribute("question") Question question) {
        question.setQuiz(quizRepository.findById(quizId).orElseThrow());
        questionRepository.save(question);
        return "redirect:/quizzes/" + quizId + "/questions";
    }

    // Story 7: delete a question
    @PostMapping("/quizzes/{quizId}/questions/{questionId}/delete")
    public String deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionId) {
        // a question's answer options point to it, so delete them first
        List<AnswerOption> options = answerOptionRepository.findByQuestionId(questionId);
        answerOptionRepository.deleteAll(options);

        questionRepository.deleteById(questionId);
        return "redirect:/quizzes/" + quizId + "/questions";
    }
}