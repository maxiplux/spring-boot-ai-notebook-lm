package app.quantun.summary.config.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration class for AI chat clients.
 * This class provides beans for OpenAI and Gemini chat clients.
 */
@Configuration
public class AiConfig {

  /**
   * Creates a ChatClient bean for OpenAI.
   *
   * @param chatModel the OpenAI chat model
   * @return the ChatClient instance
   */
  @Primary
  @Bean
  ChatClient openAiChatClient(OpenAiChatModel chatModel) {
    return ChatClient.create(chatModel);
  }

  /**
   * Creates a ChatClient bean for Gemini.
   *
   * @param chatModel the Gemini chat model
   * @return the ChatClient instance
   */
  @Bean
  ChatClient geminiChatClient(VertexAiGeminiChatModel chatModel) {
    return ChatClient.create(chatModel);
  }
}
