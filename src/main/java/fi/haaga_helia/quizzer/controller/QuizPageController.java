package fi.haaga_helia.quizzer.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fi.haaga_helia.quizzer.model.AnswerOption;
import fi.haaga_helia.quizzer.model.Category;
import fi.haaga_helia.quizzer.model.Question;
import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.repository.AnswerOptionRepository;
import fi.haaga_helia.quizzer.repository.CategoryRepository;
import fi.haaga_helia.quizzer.repository.QuestionRepository;
import fi.haaga_helia.quizzer.repository.QuizRepository;
import fi.haaga_helia.quizzer.service.QuizService;

@Controller
public class QuizPageController {

    private final QuizService quizService;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final CategoryRepository categoryRepository;

    public QuizPageController(QuizService quizService, QuizRepository quizRepository,
            QuestionRepository questionRepository,
            AnswerOptionRepository answerOptionRepository,
            CategoryRepository categoryRepository) {
        this.quizService = quizService;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
        this.categoryRepository = categoryRepository;
    }

    // Show Create Quiz page
    @GetMapping("/quizzes/new")
    public String showAddQuizPage(Model model) {
        model.addAttribute("quiz", new Quiz());
        model.addAttribute("categories", categoryRepository.findAll());

        return "add-quiz";
    }

    // Save a new quiz
    @PostMapping("/quizzes")
    public String addQuiz(@ModelAttribute("quiz") Quiz quiz,
            @RequestParam(name = "categoryId", required = false) Long categoryId) {
        quiz.setCategory(handleCategory(categoryId));
        quizService.addQuiz(quiz);

        return "redirect:/";
    }

    // Show the form for editing a quiz
    @GetMapping("/edit-quiz/{id}")
    public String showEditQuizPage(@PathVariable Long id, Model model) {
        model.addAttribute("quiz", quizService.getQuizById(id));
        model.addAttribute("categories", categoryRepository.findAll());
        
        return "edit-quiz";
    }

    // Save changes to a quiz
    @PostMapping("/edit-quiz/{id}")
    public String editQuiz(@PathVariable Long id, @ModelAttribute("quiz") Quiz submittedQuiz,
            @RequestParam(name = "categoryId", required = false) Long categoryId) {
        Quiz quiz = quizService.getQuizById(id);
        quiz.setName(submittedQuiz.getName());
        quiz.setDescription(submittedQuiz.getDescription());
        quiz.setCourseCode(submittedQuiz.getCourseCode());
        quiz.setPublished(submittedQuiz.isPublished());
        quiz.setCategory(handleCategory(categoryId));
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

        return "student-dashboard";
    }

    // Handles empty categories and category errors
    private Category handleCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));
    }
}
