package fi.haaga_helia.quizzer.web;

import java.util.Locale.Category;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import fi.haaga_helia.quizzer.domain.CourseCode;
import fi.haaga_helia.quizzer.domain.Quiz;
import fi.haaga_helia.quizzer.domain.QuizRepository;



@Controller 
public class QuizController {
    
    private final QuizRepository quizRepository;

    public QuizController(QuizController quizController) {
        this.quizRepository = quizRepository;
    }

    @PostMapping("/quiz/addquiz")
    public String saveQuiz(@ModelAttribute("quiz") Quiz quiz,
                        @RequestParam Long courseCodeId) {
        // Set the category for the book
        CourseCode courseCode = courseCodeRepository.findById(courseCodeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course code id: " + courseCodeId));
        quiz.setCourseCode(courseCode);

        courseCode.getQuizzes().add(quiz); // Add new quiz to the course

        QuizRepository.save(quiz);
        return "redirect:/teacherdashboard";
    }

    @GetMapping(value = {"/teacherdashboard", "/"})
    public String welcomeScreen(Model model) {
        model.addAttribute(model)
        return "teacherdashboard";
    }

    @PostMapping("/quiz/delete/{id}")
    public String deleteQuiz(@PathVariable("id") Long id) {
        quizRepository.deleteById(id);
        return "redirect:/teacherdashboard";
    }


    @GetMapping("/quiz/edit/{id}")
    public String editQuiz(@PathVariable("id") Long id, Model model) {
        Quiz quiz = QuizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid quiz id: " + id));

        model.addAttribute("quiz", quiz);
        
        return "editquiz";
    }

    @PostMapping("/quiz/edit/{id}")
    public String saveEditedQuiz(
            @PathVariable("id") Long id,
            @ModelAttribute("quiz") Quiz quiz) {

        quiz.setId(id);
        quizRepository.save(quiz);
        //crepository.saveAll(book);
        return "redirect:/teacherdashboard";
    }
    
    
}
