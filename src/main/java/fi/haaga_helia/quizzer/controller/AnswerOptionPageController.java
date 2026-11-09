package fi.haaga_helia.quizzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fi.haaga_helia.quizzer.model.AnswerOption;
import fi.haaga_helia.quizzer.repository.AnswerOptionRepository;
import fi.haaga_helia.quizzer.repository.QuestionRepository;

@Controller
public class AnswerOptionPageController {

    private final AnswerOptionRepository answerOptionRepository;
    private final QuestionRepository questionRepository;

    public AnswerOptionPageController(AnswerOptionRepository answerOptionRepository,
                                      QuestionRepository questionRepository) {
        this.answerOptionRepository = answerOptionRepository;
        this.questionRepository = questionRepository;
    }

    // Story 9: list the answer options of a question
    @GetMapping("/questions/{questionId}/options")
    public String listOptions(@PathVariable Long questionId, Model model) {
        model.addAttribute("question", questionRepository.findById(questionId).orElseThrow());
        model.addAttribute("options", answerOptionRepository.findByQuestionId(questionId));
        return "answer-options";
    }

    // Show the add-option form story 8
    @GetMapping("/questions/{questionId}/options/new")
    public String showAddOptionPage(@PathVariable Long questionId, Model model) {
        model.addAttribute("question", questionRepository.findById(questionId).orElseThrow());
        model.addAttribute("option", new AnswerOption());
        return "add-answer-option";
    }

    // Save the new option, story 8
    @PostMapping("/questions/{questionId}/options")
    public String addOption(@PathVariable Long questionId,
                            @ModelAttribute("option") AnswerOption option) {
        option.setQuestion(questionRepository.findById(questionId).orElseThrow());
        answerOptionRepository.save(option);
        return "redirect:/questions/" + questionId + "/options";
    }
}