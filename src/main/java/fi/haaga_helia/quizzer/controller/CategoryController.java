package fi.haaga_helia.quizzer.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fi.haaga_helia.quizzer.model.Category;
import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.repository.CategoryRepository;
import fi.haaga_helia.quizzer.repository.QuizRepository;

@Controller
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final QuizRepository quizRepository;

    public CategoryController(CategoryRepository categoryRepository, QuizRepository quizRepository) {
        this.categoryRepository = categoryRepository;
        this.quizRepository = quizRepository;
    }

    //Navigate to category list page
    @GetMapping("/categories")
    public String showCategories(Model model) {
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);
        model.addAttribute("categories", categories);
        return "categories";
    }

    //Add new category
    @GetMapping("/add-category")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "add-category";
    }

    //Save category
    @PostMapping("/save_category")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    //Delete category and change values for associated quizzes to null
    @Transactional
    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));

        if (category.getQuizzes() != null) {
            for (Quiz quiz : category.getQuizzes()) {
                quiz.setCategory(null);
            }
            quizRepository.saveAll(category.getQuizzes());
        }

        categoryRepository.delete(category);

        return "redirect:/categories";

    }
}
