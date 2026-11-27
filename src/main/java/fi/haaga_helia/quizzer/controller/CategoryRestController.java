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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

// Student REST API for categories and their published quizzes
@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Quiz categories and the published quizzes in them")
public class CategoryRestController {

    private final CategoryRepository categoryRepository;
    private final QuizService quizService;

    public CategoryRestController(CategoryRepository categoryRepository, QuizService quizService) {
        this.categoryRepository = categoryRepository;
        this.quizService = quizService;
    }

    // GET /api/categories -> [{ id, name, description }, ...]
    @Operation(summary = "Get all categories",
            description = "Returns all categories with their name and description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);
        return categories;
    }

    // GET /api/categories/{id} -> one category
    @Operation(summary = "Get a category by id",
            description = "Returns the category with the provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category with the provided id retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category with the provided id does not exist")
    })
    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category not found"));
    }

    // GET /api/categories/{id}/quizzes -> published quizzes of the category
    @Operation(summary = "Get the quizzes of a category",
            description = "Returns the published quizzes of the category with the provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Published quizzes of the category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category with the provided id does not exist")
    })
    @GetMapping("/{id}/quizzes")
    public List<QuizDto> getCategoryQuizzes(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        return quizService.getPublishedQuizzesByCategory(id);
    }
}
