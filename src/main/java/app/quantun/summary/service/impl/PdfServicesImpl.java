package app.quantun.summary.service.impl;


import app.quantun.summary.message.producer.KafkaProducerService;
import app.quantun.summary.model.dto.TableIndexContent;
import app.quantun.summary.model.request.Answer;
import app.quantun.summary.service.FileStorageService;
import app.quantun.summary.service.PdfServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
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

    @Override
    public TableIndexContent getBookTableOfContentPages(String message) {
        PromptTemplate promptTemplate = new PromptTemplate(this.tableOfContentPromptTemplate);

        BeanOutputConverter<TableIndexContent> converter = new BeanOutputConverter<>(TableIndexContent.class);


        Prompt prompt = promptTemplate.create(Map.of("format", converter.getFormat()));

        String filePath = "C:/tmp/summary-ai-pdf/Software_Architecture_Patterns-compressed.pdf";

        //String content = documentReader.read(filePath).getContent();
        Resource resource = resourceLoader.getResource("file:" + filePath);
        String aiResponse = geminiChatClient.prompt(prompt).user(promptUserSpec ->

                promptUserSpec.text("Extract the Content for this book")
                        .media(new Media(new MimeType("application", "pdf"), resource))


        ).call().content();

        TableIndexContent result = converter.convert(aiResponse);
        log.info("Table of content: {}", result);
        return result;
    }


    @Override
    public String getResponse(String message) {
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create();
        return openAiChatClient.prompt(prompt).call().content();
    }

    @Override
    public Answer getSimpleAnswerFromRandomQuestionString(String message) {
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create();
        return new Answer(openAiChatClient.prompt(prompt).call().content());

    }


    @Override
    public Answer getCapital(String stateOrCountry) {
        PromptTemplate promptTemplate = new PromptTemplate(resourceCapitalPromptTemplate);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", stateOrCountry));
        ChatResponse response = openAiChatClient.prompt(prompt).call().chatResponse();

        String responseString;

        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(response.getResult().getOutput().getContent());
        } catch (JsonProcessingException e) {
            log.error("Error parsing response for {}: {}", stateOrCountry, e.getMessage(), e);


        }

        responseString = jsonNode.get("answer").asText();

        return new Answer(responseString);
    }


    @Override
    public Answer getCapitalWithInfo(String stateOrCountry) {
        PromptTemplate promptTemplate = new PromptTemplate(this.resourceCapitalWithInfoPromptTemplate);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", stateOrCountry));
        ChatResponse response = openAiChatClient.prompt(prompt).call().chatResponse();

        return new Answer(response.getResult().getOutput().getContent());
    }

    @Override
    public String storePdfFile(MultipartFile file) {
        val message = this.fileStorageService.storePdfFile(file);

        this.kafkaProducerService.sendHashMapMessage();
        return message;

    }


}
