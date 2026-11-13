
package fi.haaga_helia.quizzer.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fi.haaga_helia.quizzer.model.AnswerOption;
import fi.haaga_helia.quizzer.model.Question;
import fi.haaga_helia.quizzer.repository.AnswerOptionRepository;
import fi.haaga_helia.quizzer.repository.QuestionRepository;

@Controller
public class AnswerOptionPageController {

    private final AnswerOptionRepository answerOptionRepository;
    private final QuestionRepository questionRepository;

    public AnswerOptionPageController(
            AnswerOptionRepository answerOptionRepository,
            QuestionRepository questionRepository) {

        this.answerOptionRepository = answerOptionRepository;
        this.questionRepository = questionRepository;
    }

    // Show all answer options for a question
    @GetMapping("/questions/{questionId}/options")
    public String listOptions(
            @PathVariable Long questionId,
            Model model) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow();

        model.addAttribute("question", question);

        model.addAttribute(
                "options",
                answerOptionRepository.findByQuestionId(questionId)
        );

        return "answer-options";
    }

    // Show the page for adding multiple answer options
    @GetMapping("/questions/{questionId}/options/new")
    public String showAddOptionsPage(
            @PathVariable Long questionId,
            Model model) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow();

        model.addAttribute("question", question);

        // Start with four empty answer fields
        model.addAttribute(
                "optionTexts",
                List.of("", "", "", "")
        );

        model.addAttribute(
                "correctIndexes",
                List.of()
        );

        return "add-answer-option";
    }

    // Save all answer options together
    @PostMapping("/questions/{questionId}/options")
    public String addOptions(
            @PathVariable Long questionId,

            @RequestParam(name = "optionTexts", required = false)
            List<String> optionTexts,

            @RequestParam(name = "correctIndexes", required = false)
            List<Integer> correctIndexes,

            Model model) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow();

        // Handle missing form values
        if (optionTexts == null) {
            optionTexts = new ArrayList<>(
                    List.of("", "", "", "")
            );
        }

        if (correctIndexes == null) {
            correctIndexes = new ArrayList<>();
        }

        // Count non-empty answer options
        long validOptionCount = optionTexts.stream()
                .filter(text -> text != null && !text.isBlank())
                .count();

        // Check whether at least one non-empty option is marked correct
        boolean hasCorrectAnswer = false;

        for (Integer index : correctIndexes) {

            if (index != null
                    && index >= 0
                    && index < optionTexts.size()
                    && optionTexts.get(index) != null
                    && !optionTexts.get(index).isBlank()) {

                hasCorrectAnswer = true;
                break;
            }
        }

        // Validate answer options
        String errorMessage = null;

        if (validOptionCount < 2) {

            errorMessage = "Please enter at least two answer options.";

        } else if (!hasCorrectAnswer) {

            errorMessage = "Please select at least one correct answer.";
        }

        // If validation fails, show the form again
        if (errorMessage != null) {

            model.addAttribute("question", question);
            model.addAttribute("optionTexts", optionTexts);
            model.addAttribute("correctIndexes", correctIndexes);
            model.addAttribute("errorMessage", errorMessage);

            return "add-answer-option";
        }

        // Create and save all non-empty options
        List<AnswerOption> newOptions = new ArrayList<>();

        for (int i = 0; i < optionTexts.size(); i++) {

            String text = optionTexts.get(i);

            if (text != null && !text.isBlank()) {

                AnswerOption option = new AnswerOption();

                option.setOptionText(text.trim());
                option.setQuestion(question);

                // Mark the option as correct if its index is selected
                option.setCorrect(correctIndexes.contains(i));

                newOptions.add(option);
            }
        }

        // Save all answer options together
        answerOptionRepository.saveAll(newOptions);

        // Return to the answer options list
        return "redirect:/questions/" + questionId + "/options";
    }

    // Delete an answer option
    @PostMapping("/questions/{questionId}/options/{id}/delete")
    public String deleteAnswer(
            @PathVariable Long questionId,
            @PathVariable Long id) {

        answerOptionRepository.deleteById(id);

        return "redirect:/questions/" + questionId + "/options";
    }
}
