package fi.haaga_helia.quizzer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import fi.haaga_helia.quizzer.model.Category;
import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.repository.CategoryRepository;
import fi.haaga_helia.quizzer.repository.MessageRepository;
import fi.haaga_helia.quizzer.repository.QuizRepository;

class SampleQuizDataTests {

	@Test
	void seedsPublishedQuizzesWhenEmpty() throws Exception {
		MessageRepository messages = mock(MessageRepository.class);
		QuizRepository quizzes = mock(QuizRepository.class);
		CategoryRepository categories = mock(CategoryRepository.class);
		Category existing = new Category("Programming", "Existing category");
		when(categories.findByName("Programming")).thenReturn(List.of(existing));
		when(categories.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

		new QuizzerBackendApplication().generateTestData(messages, quizzes, categories).run();

		ArgumentCaptor<List<Quiz>> saved = ArgumentCaptor.captor();
		verify(quizzes).saveAll(saved.capture());
		List<Quiz> data = saved.getValue();
		assertEquals(5, data.size());
		assertTrue(data.stream().allMatch(Quiz::isPublished));
		assertEquals(List.of("Programming", "Web Development", "Databases", "Software Testing", "Cybersecurity"),
				data.stream().map(quiz -> quiz.getCategory().getName()).toList());
		assertSame(existing, data.get(0).getCategory());
	}

	@Test
	void leavesExistingQuizzesAlone() throws Exception {
		MessageRepository messages = mock(MessageRepository.class);
		QuizRepository quizzes = mock(QuizRepository.class);
		CategoryRepository categories = mock(CategoryRepository.class);
		when(quizzes.count()).thenReturn(1L);

		new QuizzerBackendApplication().generateTestData(messages, quizzes, categories).run();

		verify(quizzes, never()).saveAll(any());
		verify(categories, never()).save(any(Category.class));
	}
}
