package app.quantun.summary.config.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(AiConfig.class)
@SpringBootTest
public class AiConfigTest {

    @Autowired
    private ChatClient openAiChatClient;

    @Autowired
    private ChatClient geminiChatClient;

    @Test
    public void testOpenAiChatClientBean() {
        assertNotNull(openAiChatClient, "OpenAiChatClient bean should not be null");
    }

    @Test
    public void testGeminiChatClientBean() {
        assertNotNull(geminiChatClient, "GeminiChatClient bean should not be null");
    }
}
