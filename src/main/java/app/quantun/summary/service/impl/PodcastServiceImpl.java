package app.quantun.summary.service.impl;

import app.quantun.summary.model.contract.dto.BookSummary;
import app.quantun.summary.model.contract.dto.PodCast;
import app.quantun.summary.model.entity.Book;
import app.quantun.summary.service.PodcastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class PodcastServiceImpl implements PodcastService {

    @Autowired
    @Qualifier("anthropicChatClient")
    private ChatClient chatClient;


    @Value("classpath:templates/system/book.podcast.structure.st")
    private Resource systemSummaryBookTemplate;


    @Value("classpath:templates/user/book.podcast.user.structure.st")
    private Resource userSummaryBookTemplate;

    @Override
    public PodCast createScript(Book book) {

        try {
            if (book == null || book.getTextSummary() == null
                    || book.getTextSummary().getChapterDigest() == null) {
                throw new IllegalArgumentException("Book or its summary cannot be null.");
            }

            BookSummary bookSummary = book.getTextSummary();

            ObjectMapper objectMapper = new ObjectMapper();

            String bookSummaryJson = objectMapper.writeValueAsString(bookSummary);
            PromptTemplate systemPromptTemplate = new PromptTemplate(this.systemSummaryBookTemplate);

            PromptTemplate userPromptTemplate = new PromptTemplate(this.userSummaryBookTemplate);


            BeanOutputConverter<PodCast> format = new BeanOutputConverter<>(PodCast.class);

            Prompt systemPrompt = systemPromptTemplate.create(
                    Map.of("summary", bookSummaryJson, "format", format.getFormat())

            );

            Prompt userPrompt = userPromptTemplate.create();



            String aiResponse = chatClient.prompt(systemPrompt).advisors(new SimpleLoggerAdvisor()).user(userPrompt.getContents()).call().content();


            PodCast result = format.convert(aiResponse);
            log.info("PodCast of content: {}", result.getTitle());
            return result;


        } catch (JsonProcessingException e) {
            log.error("Error while converting book summary to JSON.{}", e.getMessage());
            throw new RuntimeException("Error while converting book summary to JSON.", e);
        }




    }
}
