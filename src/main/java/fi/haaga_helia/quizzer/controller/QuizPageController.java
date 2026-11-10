
package fi.haaga_helia.quizzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.service.QuizService;

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

    // Show the form for editing a quiz
    @GetMapping("/edit-quiz/{id}")
    public String showEditQuizPage(@PathVariable Long id, Model model) {
        model.addAttribute("quiz", quizService.getQuizById(id));
        return "edit-quiz";
    }

    // Save changes to a quiz
    @PostMapping("/edit-quiz/{id}")
    public String editQuiz(@PathVariable Long id, @ModelAttribute("quiz") Quiz submittedQuiz) {
        Quiz quiz = quizService.getQuizById(id);
        quiz.setName(submittedQuiz.getName());
        quiz.setDescription(submittedQuiz.getDescription());
        quiz.setCourseCode(submittedQuiz.getCourseCode());
        quiz.setPublished(submittedQuiz.isPublished());
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