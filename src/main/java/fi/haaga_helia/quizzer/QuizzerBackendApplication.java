package fi.haaga_helia.quizzer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fi.haaga_helia.quizzer.model.AnswerOption;
import fi.haaga_helia.quizzer.model.Category;
import fi.haaga_helia.quizzer.model.Message;
import fi.haaga_helia.quizzer.model.Question;
import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.repository.AnswerOptionRepository;
import fi.haaga_helia.quizzer.repository.CategoryRepository;
import fi.haaga_helia.quizzer.repository.MessageRepository;
import fi.haaga_helia.quizzer.repository.QuestionRepository;
import fi.haaga_helia.quizzer.repository.QuizRepository;

@SpringBootApplication
public class QuizzerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizzerBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner generateTestData(MessageRepository messageRepository, QuizRepository quizRepository,
			CategoryRepository categoryRepository, QuestionRepository questionRepository,
			AnswerOptionRepository answerOptionRepository) {
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
				List<Quiz> quizzes = quizRepository.saveAll(List.of(
						new Quiz("Java Basics", "Practice Java fundamentals.", "JAVA101", true, null, programming),
						new Quiz("Web Development", "Review HTML, CSS and JavaScript.", "WEB101", true, null, web),
						new Quiz("Database Basics", "Test your knowledge of SQL.", "DB101", true, null, databases),
						new Quiz("Software Testing", "Explore software testing fundamentals.", "TEST101", true, null, testing),
						new Quiz("Cybersecurity Basics", "Review common security concepts.", "SEC101", true, null, security)));

				seedQuestions(quizzes, questionRepository, answerOptionRepository);
			}
		};
	}

	private void seedQuestions(List<Quiz> quizzes, QuestionRepository questionRepository,
			AnswerOptionRepository answerOptionRepository) {
		List<Question> questions = new ArrayList<>();
		List<AnswerOption> answerOptions = new ArrayList<>();

		addQuestion(questions, answerOptions, quizzes.get(0), "Which keyword creates a subclass in Java?", 1,
				"implements", "extends", "inherits", "super");
		addQuestion(questions, answerOptions, quizzes.get(0), "Which type stores true or false values?", 2,
				"int", "String", "boolean", "char");
		addQuestion(questions, answerOptions, quizzes.get(0), "What is the entry point of a standard Java application?", 0,
				"public static void main(String[] args)", "public void start(String[] args)",
				"static int run(String[] args)", "private main()");

		addQuestion(questions, answerOptions, quizzes.get(1), "Which HTML element creates a hyperlink?", 1,
				"<link>", "<a>", "<href>", "<url>");
		addQuestion(questions, answerOptions, quizzes.get(1), "Which CSS property changes the text color?", 2,
				"font-style", "background-color", "color", "text-decoration");
		addQuestion(questions, answerOptions, quizzes.get(1), "Which keyword declares a block-scoped variable in JavaScript?", 0,
				"let", "var", "static", "define");

		addQuestion(questions, answerOptions, quizzes.get(2), "Which SQL statement retrieves rows from a table?", 3,
				"UPDATE", "INSERT", "ALTER", "SELECT");
		addQuestion(questions, answerOptions, quizzes.get(2), "Which clause filters rows before grouping?", 0,
				"WHERE", "HAVING", "ORDER BY", "GROUP BY");
		addQuestion(questions, answerOptions, quizzes.get(2), "Which constraint uniquely identifies each row in a table?", 2,
				"FOREIGN KEY", "NOT NULL", "PRIMARY KEY", "DEFAULT");

		addQuestion(questions, answerOptions, quizzes.get(3), "What is the main purpose of a unit test?", 1,
				"Measure production performance", "Check a small unit of code in isolation",
				"Replace all integration tests", "Deploy the application");
		addQuestion(questions, answerOptions, quizzes.get(3), "What does a regression test check for?", 3,
				"Whether a feature has a user guide", "Whether code follows formatting rules",
				"Whether the database is backed up", "Whether a change broke existing behavior");
		addQuestion(questions, answerOptions, quizzes.get(3), "Which testing technique checks behavior without inspecting internal code?", 0,
				"Black-box testing", "White-box testing", "Static analysis", "Code review");

		addQuestion(questions, answerOptions, quizzes.get(4), "What is the purpose of multi-factor authentication?", 2,
				"To reuse one password across services", "To encrypt stored files automatically",
				"To require more than one proof of identity", "To make accounts publicly accessible");
		addQuestion(questions, answerOptions, quizzes.get(4), "Which is the strongest password practice?", 1,
				"Use a short password that is easy to remember",
				"Use a unique, long password for each account",
				"Reuse a password with a different username", "Write passwords in a public document");
		addQuestion(questions, answerOptions, quizzes.get(4), "What does HTTPS help protect in transit?", 0,
				"Communication between a browser and a website",
				"The physical security of the server", "The accuracy of website content",
				"Files already stored on a user's device");

		questionRepository.saveAll(questions);
		answerOptionRepository.saveAll(answerOptions);
	}

	private void addQuestion(List<Question> questions, List<AnswerOption> answerOptions, Quiz quiz,
			String questionText, int correctIndex, String... optionTexts) {
		Question question = new Question();
		question.setQuestionText(questionText);
		question.setQuiz(quiz);
		questions.add(question);

		for (int i = 0; i < optionTexts.length; i++) {
			AnswerOption option = new AnswerOption();
			option.setOptionText(optionTexts[i]);
			option.setCorrect(i == correctIndex);
			option.setQuestion(question);
			answerOptions.add(option);
		}
	}

	private Category getOrCreateCategory(CategoryRepository repository, String name, String description) {
		List<Category> existing = repository.findByName(name);
		if (!existing.isEmpty()) {
			return existing.get(0);
		}
		return repository.save(new Category(name, description));
	}

}
