
package fi.haaga_helia.quizzer.controller;

import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.service.QuizService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class QuizPageController {

    private final QuizService quizService;

    public QuizPageController(QuizService quizService) {
        this.quizService = quizService;
    }

    // Show Create Quiz page
    @GetMapping("/quizzes/new")
    public String showAddQuizPage(Model model) {

        model.addAttribute("quiz", new Quiz());

        return "add-quiz";
    }

    // Save a new quiz
    @PostMapping("/quizzes")
    public String addQuiz(@ModelAttribute("quiz") Quiz quiz) {

        quizService.addQuiz(quiz);

        return "redirect:/";
    }

    // Show Teacher Dashboard
    @GetMapping("/")
    public String showDashboard(Model model) {

        model.addAttribute("quizzes", quizService.getAllQuizzes());

        return "teacher-dashboard";
    }
}
