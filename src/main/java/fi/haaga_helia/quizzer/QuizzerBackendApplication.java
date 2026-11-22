package fi.haaga_helia.quizzer;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fi.haaga_helia.quizzer.model.Category;
import fi.haaga_helia.quizzer.model.Message;
import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.repository.CategoryRepository;
import fi.haaga_helia.quizzer.repository.MessageRepository;
import fi.haaga_helia.quizzer.repository.QuizRepository;

@SpringBootApplication
public class QuizzerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizzerBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner generateTestData(MessageRepository messageRepository, QuizRepository quizRepository,
			CategoryRepository categoryRepository) {
		return args -> {
			if (messageRepository.count() == 0) {
				messageRepository.save(new Message("Hello world!"));
			}
			if (quizRepository.count() == 0) {
				Category programming = getOrCreateCategory(categoryRepository, "Programming", "Programming languages and concepts.");
				Category web = getOrCreateCategory(categoryRepository, "Web Development", "Web technologies.");
				Category databases = getOrCreateCategory(categoryRepository, "Databases", "Database concepts and SQL.");
				Category testing = getOrCreateCategory(categoryRepository, "Software Testing", "Software testing concepts.");
				Category security = getOrCreateCategory(categoryRepository, "Cybersecurity", "Security concepts.");
				quizRepository.saveAll(List.of(
						new Quiz("Java Basics", "Practice Java fundamentals.", "JAVA101", true, null, programming),
						new Quiz("Web Development", "Review HTML, CSS and JavaScript.", "WEB101", true, null, web),
						new Quiz("Database Basics", "Test your knowledge of SQL.", "DB101", true, null, databases),
						new Quiz("Software Testing", "Explore software testing fundamentals.", "TEST101", true, null, testing),
						new Quiz("Cybersecurity Basics", "Review common security concepts.", "SEC101", true, null, security)));
			}
		};
	}

	private Category getOrCreateCategory(CategoryRepository repository, String name, String description) {
		List<Category> existing = repository.findByName(name);
		if (!existing.isEmpty()) {
			return existing.get(0);
		}
		return repository.save(new Category(name, description));
	}

}
