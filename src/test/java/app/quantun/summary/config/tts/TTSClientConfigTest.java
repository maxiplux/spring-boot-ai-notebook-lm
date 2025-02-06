package app.quantun.summary.config.tts;

import app.quantun.summary.service.rest.TextToSpeechClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TTSClientConfigTest {

    @Autowired
    private WebClient azureTtsWebClient;

    @Autowired
    private TextToSpeechClient azureTextToSpeechClient;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${azure.tts.subscription-key}")
    private String subscriptionKey;

    @Value("${azure.tts.server-url}")
    private String ttsServerUrl;

    @Test
    public void testAzureTtsWebClientBean() {
        assertNotNull(azureTtsWebClient, "AzureTtsWebClient bean should not be null");
    }

    @Test
    public void testAzureTextToSpeechClientBean() {
        assertNotNull(azureTextToSpeechClient, "AzureTextToSpeechClient bean should not be null");
    }

    @Test
    public void testRestTemplateBean() {
        assertNotNull(restTemplate, "RestTemplate bean should not be null");
    }

    @Configuration
    static class TestConfig {

        @Value("${azure.tts.subscription-key}")
        private String subscriptionKey;

        @Value("${azure.tts.server-url}")
        private String ttsServerUrl;

        @Bean
        public WebClient azureTtsWebClient() {
            return WebClient.builder()
                    .baseUrl(ttsServerUrl)
                    .defaultHeaders(headers -> {
                        headers.add("Ocp-Apim-Subscription-Key", subscriptionKey);
                        headers.add("User-Agent", "curl");
                        headers.add("Content-Type", "application/ssml+xml");
                        headers.add("X-Microsoft-OutputFormat", "audio-16khz-128kbitrate-mono-mp3");
                    })
                    .build();
        }

        @Bean
        public TextToSpeechClient azureTextToSpeechClient(WebClient webClient) {
            return new TTSClientConfig().azureTextToSpeechClient(webClient);
        }

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }
}
