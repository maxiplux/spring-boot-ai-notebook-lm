package app.quantun.summary.service.impl;

import app.quantun.summary.model.contract.dto.BookSummary;
import app.quantun.summary.model.contract.dto.TableIndexContent;
import app.quantun.summary.model.entity.Book;
import app.quantun.summary.service.SummaryServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.model.Media;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.ai.retry.TransientAiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryServicesImpl implements SummaryServices {

    @Autowired
    @Qualifier("geminiChatClient")
    private  ChatClient geminiChatClient;


    @Value("classpath:templates/system/book.summary.structure.st")
    private Resource systemSummaryBookTemplate;

    private final ResourceLoader resourceLoader;


    @Override
    public BookSummary summarizeBook(Book book) {
        MDC.put("bookId", book.getId().toString());
        try {





            log.info("Starting book summarization process");
            log.debug("Loading book from path: {}", book.getPath());
            PromptTemplate promptTemplate = new PromptTemplate(this.systemSummaryBookTemplate);
        BeanOutputConverter<BookSummary> converter = new BeanOutputConverter<>(BookSummary.class);
        Prompt prompt = promptTemplate.create(Map.of("format", converter.getFormat()));
        String filePath = book.getPath();
        Resource resource = resourceLoader.getResource("file:" + filePath);

            log.debug("Resource loaded successfully, exists: {}", resource.exists());

            String aiResponse = geminiChatClient.prompt(prompt).advisors(new SimpleLoggerAdvisor()).user(promptUserSpec ->
                promptUserSpec.text("Extract the Content for this book")
                        .media(new Media(new MimeType("application", "pdf"), resource))
        ).call().content();
            log.debug("Received AI response of length: {}", aiResponse.length());

            BookSummary result = converter.convert(aiResponse);
        log.info("Table of content: {}", result);
        return result;

        } catch (NonTransientAiException e) {
            log.error("NonTransientAiException Failed to summarize book {}", e.getMessage());
            return null;
        }
        catch (TransientAiException e) {
            log.error("TransientAiException Failed to summarize book {}", e.getMessage());
            return null;
        }

        finally {
            MDC.remove("bookId");
        }

    }
}
