package fi.haaga_helia.quizzer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fi.haaga_helia.quizzer.model.AnswerOption;
import fi.haaga_helia.quizzer.model.Category;
import fi.haaga_helia.quizzer.model.Question;
import fi.haaga_helia.quizzer.model.Quiz;
import fi.haaga_helia.quizzer.repository.AnswerOptionRepository;
import fi.haaga_helia.quizzer.repository.CategoryRepository;
import fi.haaga_helia.quizzer.repository.QuestionRepository;
import fi.haaga_helia.quizzer.repository.QuizRepository;

class SampleQuizDataTests {

	@Test
	void seedsPublishedQuizzesWhenEmpty() throws Exception {
		QuizRepository quizzes = mock(QuizRepository.class);
		CategoryRepository categories = mock(CategoryRepository.class);
		QuestionRepository questions = mock(QuestionRepository.class);
		AnswerOptionRepository answerOptions = mock(AnswerOptionRepository.class);
		Category existing = new Category("Programming", "Existing category");
		when(categories.findByName("Programming")).thenReturn(List.of(existing));
		when(categories.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
		when(quizzes.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

		new QuizzerBackendApplication().generateTestData(quizzes, categories, questions, answerOptions).run();

		ArgumentCaptor<List<Quiz>> saved = ArgumentCaptor.captor();
		verify(quizzes).saveAll(saved.capture());
		List<Quiz> data = saved.getValue();
		assertEquals(5, data.size());
		assertTrue(data.stream().allMatch(Quiz::isPublished));
		assertEquals(List.of("Programming", "Web Development", "Databases", "Software Testing", "Cybersecurity"),
				data.stream().map(quiz -> quiz.getCategory().getName()).toList());
		assertSame(existing, data.get(0).getCategory());

		ArgumentCaptor<List<Question>> savedQuestions = ArgumentCaptor.captor();
		verify(questions).saveAll(savedQuestions.capture());
		assertEquals(15, savedQuestions.getValue().size());

		ArgumentCaptor<List<AnswerOption>> savedOptions = ArgumentCaptor.captor();
		verify(answerOptions).saveAll(savedOptions.capture());
		assertEquals(60, savedOptions.getValue().size());
		for (Question question : savedQuestions.getValue()) {
			List<AnswerOption> optionsForQuestion = savedOptions.getValue().stream()
					.filter(option -> option.getQuestion() == question)
					.toList();
			assertEquals(4, optionsForQuestion.size());
			assertEquals(1, optionsForQuestion.stream().filter(AnswerOption::isCorrect).count());
		}
		for (Quiz quiz : data) {
			assertEquals(3, savedQuestions.getValue().stream()
					.filter(question -> question.getQuiz() == quiz)
					.count());
		}
	}

	@Test
	void leavesExistingQuizzesAlone() throws Exception {
		QuizRepository quizzes = mock(QuizRepository.class);
		CategoryRepository categories = mock(CategoryRepository.class);
		QuestionRepository questions = mock(QuestionRepository.class);
		AnswerOptionRepository answerOptions = mock(AnswerOptionRepository.class);
		when(quizzes.count()).thenReturn(1L);

		new QuizzerBackendApplication().generateTestData(quizzes, categories, questions, answerOptions).run();

		verify(quizzes, never()).saveAll(any());
		verify(categories, never()).save(any(Category.class));
		verify(questions, never()).saveAll(any());
		verify(answerOptions, never()).saveAll(any());
	}
}
