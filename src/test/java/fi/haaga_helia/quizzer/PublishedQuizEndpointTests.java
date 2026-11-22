package fi.haaga_helia.quizzer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = "spring.datasource.url=jdbc:h2:mem:publishedquiztest")
class PublishedQuizEndpointTests {

	@LocalServerPort
	private int port;

	@Test
	void servesSeededQuizzesToLocalStudentDashboard() throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/api/quizzes/published"))
				.header("Origin", "http://localhost:5173")
				.GET()
				.build();
		HttpResponse<String> response = HttpClient.newHttpClient()
				.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(200, response.statusCode());
		assertEquals("http://localhost:5173",
				response.headers().firstValue("Access-Control-Allow-Origin").orElse(""));
		assertTrue(response.body().contains("Java Basics"));
		assertTrue(response.body().contains("Cybersecurity Basics"));
		assertTrue(response.body().contains("\"name\":\"Programming\""));
	}

	@Test
	void rejectsUnapprovedOrigin() throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/api/quizzes/published"))
				.header("Origin", "http://example.com")
				.GET()
				.build();
		HttpResponse<String> response = HttpClient.newHttpClient()
				.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(403, response.statusCode());
	}
}
