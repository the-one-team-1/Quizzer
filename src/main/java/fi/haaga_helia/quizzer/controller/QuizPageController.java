package fi.haaga_helia.quizzer.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fi.haaga_helia.quizzer.model.AnswerOption;
import fi.haaga_helia.quizzer.model.Question;
import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.repository.AnswerOptionRepository;
import fi.haaga_helia.quizzer.repository.QuestionRepository;
import fi.haaga_helia.quizzer.repository.QuizRepository;
import fi.haaga_helia.quizzer.service.QuizService;

@Controller
public class QuizPageController {

    private final QuizService quizService;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;

    public QuizPageController(QuizService quizService, QuizRepository quizRepository,
            QuestionRepository questionRepository,
            AnswerOptionRepository answerOptionRepository) {
        this.quizService = quizService;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
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

    // Delete a quiz and its contents
    @Transactional
    @PostMapping("/quizzes/{id}/delete")
    public String deleteQuiz(@PathVariable Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found: " + id));
        List<Question> questions = questionRepository.findByQuizId(id);

        for (Question question : questions) {
            List<AnswerOption> options = answerOptionRepository.findByQuestionId(question.getId());
            answerOptionRepository.deleteAll(options);
        }

        quizRepository.delete(quiz);
        return "redirect:/";
    }

    // Show Teacher Dashboard
    @GetMapping("/")
    public String showDashboard(Model model) {

        model.addAttribute("quizzes", quizService.getAllQuizzes());

        return "teacher-dashboard";
    }
}
