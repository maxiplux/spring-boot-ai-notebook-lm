package app.quantun.summary.service.impl;

import app.quantun.summary.message.producer.SummaryBookProducerService;
import app.quantun.summary.model.contract.dto.TableIndexContent;
import app.quantun.summary.model.contract.message.BookFilePayload;
import app.quantun.summary.model.entity.Book;
import app.quantun.summary.repository.BookRepository;

import app.quantun.summary.service.FileStorageService;
import app.quantun.summary.service.PdfServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.model.Media;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

/**
 * Service implementation for PDF-related operations.
 * This class provides methods to extract table of content pages from a book and store PDF files.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PdfServicesImpl implements PdfServices {

    public static final int TOP_FIVE_RECORDS_IN_VECTOR_DB = 5;
    private final SummaryBookProducerService summaryBookProducerService;
    private final FileStorageService fileStorageService;



    @Qualifier("geminiChatClient")
    private final ChatClient geminiChatClient;

    private final ResourceLoader resourceLoader;

    private final BookRepository summaryBookRepository;

    @Value("classpath:templates/system/table.content.st")
    private Resource tableOfContentPromptTemplate;

    /**
     * Retrieves the table of content pages for a given book.
     *
     * @param message the message containing the book information
     * @return the table of content pages
     */
    @Override
    public TableIndexContent getBookTableOfContentPages(String message) {
        PromptTemplate promptTemplate = new PromptTemplate(this.tableOfContentPromptTemplate);
        BeanOutputConverter<TableIndexContent> converter = new BeanOutputConverter<>(TableIndexContent.class);
        Prompt prompt = promptTemplate.create(Map.of("format", converter.getFormat()));
        String filePath = "C:/tmp/summary-ai-pdf/Software_Architecture_Patterns-compressed.pdf";
        Resource resource = resourceLoader.getResource("file:" + filePath);
        String aiResponse = geminiChatClient.prompt(prompt).advisors(new SimpleLoggerAdvisor()).user(promptUserSpec ->
                promptUserSpec.text("Extract the Content for this book")
                        .media(new Media(new MimeType("application", "pdf"), resource))
        ).call().content();
        TableIndexContent result = converter.convert(aiResponse);
        log.info("Table of content: {}", result);
        return result;
    }

    /**
     * Stores the uploaded PDF file and sends a message to Kafka.
     *
     * @param file the uploaded PDF file
     * @return the stored file name
     */
    @Override
    public String storePdfFile(MultipartFile file) {
        val bookPath = this.fileStorageService.storePdfFile(file);
        val summaryBook = Book.builder()
                .uuid(UUID.randomUUID().toString())
                .name(file.getOriginalFilename())
                .path(bookPath)
                .build();
        val savedSummaryBook = this.summaryBookRepository.save(summaryBook);

        val message = new BookFilePayload(savedSummaryBook.getId(), savedSummaryBook.getName());
        this.summaryBookProducerService.sendBookToBeProcessed(message);
        return message.toString();
    }
}
