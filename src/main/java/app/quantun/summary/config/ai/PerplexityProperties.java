package app.quantun.summary.config.ai;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.ai.perplexity")


@Data
public class PerplexityProperties {
    private String apiKey;
    private String baseUrl;
    private Chat chat = new Chat();

    // Getters and setters
    @Data
    public static class Chat {
        private String model;
        private String completionsPath;
        private Double temperature;

        // Getters and setters
    }
}
