package fi.haaga_helia.quizzer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
   public CorsConfig() {
   }

   public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/api/**").allowedOriginPatterns(new String[]{"http://localhost:*", "https://*.onrender.com"}).allowedMethods(new String[]{"GET", "POST", "OPTIONS"}).allowedHeaders(new String[]{"*"});
   }
}
