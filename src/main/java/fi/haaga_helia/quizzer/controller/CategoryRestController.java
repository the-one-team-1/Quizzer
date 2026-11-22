package fi.haaga_helia.quizzer.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haaga_helia.quizzer.dto.ApiDto.QuizDto;
import fi.haaga_helia.quizzer.model.Category;
import fi.haaga_helia.quizzer.repository.CategoryRepository;
import fi.haaga_helia.quizzer.service.QuizService;

// Student REST API for categories and their published quizzes
@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    private final CategoryRepository categoryRepository;
    private final QuizService quizService;

    public CategoryRestController(CategoryRepository categoryRepository, QuizService quizService) {
        this.categoryRepository = categoryRepository;
        this.quizService = quizService;
    }

    // GET /api/categories -> [{ id, name, description }, ...]
    @GetMapping
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);
        return categories;
    }

    // GET /api/categories/{id} -> one category
    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category not found"));
    }

    // GET /api/categories/{id}/quizzes -> published quizzes of the category
    @GetMapping("/{id}/quizzes")
    public List<QuizDto> getCategoryQuizzes(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        return quizService.getPublishedQuizzesByCategory(id);
    }
}
