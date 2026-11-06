
package fi.haaga_helia.quizzer.controller;

import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.service.QuizService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class QuizPageController {

    private final QuizService quizService;

    public QuizPageController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/quizzes/new")
    public String showAddQuizPage(Model model) {

        model.addAttribute("quiz", new Quiz());

        return "add-quiz";
    }

    @PostMapping("/quizzes")
    public String addQuiz(@ModelAttribute("quiz") Quiz quiz) {

        quizService.addQuiz(quiz);

        return "redirect:/quizzes/new?success";
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("quizzes", quizService.getAllQuizzes());
        return "quiz-list";
    }
    
}
