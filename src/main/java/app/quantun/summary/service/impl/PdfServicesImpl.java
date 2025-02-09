package app.quantun.summary.service.impl;

import app.quantun.summary.message.producer.KafkaProducerService;
import app.quantun.summary.model.contract.dto.TableIndexContent;
import app.quantun.summary.service.FileStorageService;
import app.quantun.summary.service.PdfServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.ai.chat.client.ChatClient;
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

/**
 * Service implementation for PDF-related operations.
 * This class provides methods to extract table of content pages from a book and store PDF files.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PdfServicesImpl implements PdfServices {

  public static final int TOP_FIVE_RECORDS_IN_VECTOR_DB = 5;
  private final KafkaProducerService kafkaProducerService;
  private final FileStorageService fileStorageService;
  @Qualifier("openAiChatClient")
  private final ChatClient openAiChatClient;
  @Qualifier("geminiChatClient")
  private final ChatClient geminiChatClient;
  private final ObjectMapper objectMapper;
  private final ResourceLoader resourceLoader;
  @Value("classpath:/templates/rag-prompt-template.st")
  private Resource ressourceRagPromptTemplate;
  @Value("classpath:/templates/system-message.st")
  private Resource resourceSystemMessageTemplate;
  @Value("classpath:/templates/rag-prompt-without-metadata-template.st")
  private Resource resourceRagPromptWithoutMedataTemplate;
  @Value("classpath:templates/get-capital-prompt.st")
  private Resource resourceCapitalPromptTemplate;
  @Value("classpath:templates/get-capital-with-info.st")
  private Resource resourceCapitalWithInfoPromptTemplate;
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
    String aiResponse = geminiChatClient.prompt(prompt).user(promptUserSpec ->
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
    val message = this.fileStorageService.storePdfFile(file);
    this.kafkaProducerService.sendHashMapMessage();
    return message;
  }
}
