package app.quantun.summary.config.tts;

import app.quantun.summary.service.rest.TextToSpeechClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class TTSClientConfig {

    @Value("${azure.tts.subscription-key}")
    private String subscriptionKey;


    @Value("${azure.tts.server-url}")
    private String ttsServerUrl;

    @Bean
    WebClient azureTtsWebClient() {

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024) // 100 MB buffer
                )
                .build();

        return WebClient.builder()
                .baseUrl(this.ttsServerUrl)
                .exchangeStrategies(strategies)
                .defaultHeaders(headers -> {
                    headers.add("Ocp-Apim-Subscription-Key", subscriptionKey);
                    headers.add("User-Agent", "curl");
                    headers.add("Content-Type", "application/ssml+xml");
                    headers.add("X-Microsoft-OutputFormat", "audio-16khz-128kbitrate-mono-mp3");
                })
                .build();
    }

    @Bean
    TextToSpeechClient azureTextToSpeechClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))

                .build();
        return factory.createClient(TextToSpeechClient.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
