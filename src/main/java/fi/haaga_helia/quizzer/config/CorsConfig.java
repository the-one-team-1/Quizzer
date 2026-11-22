package fi.haaga_helia.quizzer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// One CORS rule for the whole student API, so the React app can call /api/**
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/quizzes/published")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "OPTIONS");
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "OPTIONS");
    }
}
