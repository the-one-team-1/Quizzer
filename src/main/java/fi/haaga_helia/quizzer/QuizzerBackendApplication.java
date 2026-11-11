package fi.haaga_helia.quizzer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fi.haaga_helia.quizzer.model.Message;
import fi.haaga_helia.quizzer.repository.MessageRepository;

@SpringBootApplication
public class QuizzerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizzerBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner generateTestData(MessageRepository messageRepository) {
		return args -> {
			if (messageRepository.count() == 0) {
				messageRepository.save(new Message("Hello world!"));
			}
		};
	}

}
