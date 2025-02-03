package app.quantun.summary.services.impl;


import app.quantun.summary.model.request.Answer;
import app.quantun.summary.services.PdfServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiServicesSImpl implements PdfServices {


    public static final int TOP_FIVE_RECORDS_IN_VECTOR_DB = 5;
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

    private  final ChatClient chatClient;

    private final ObjectMapper objectMapper;



    @Override
    public String getResponse(String message) {
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create();

        return chatClient.prompt(prompt).call().content();
    }

    @Override
    public Answer getSimpleAnswerFromRandomQuestionString(String message) {
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create();
        return new Answer(chatClient.prompt(prompt).call().content());

    }


    @Override
    public Answer getCapital(String stateOrCountry) {
        PromptTemplate promptTemplate = new PromptTemplate(resourceCapitalPromptTemplate);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", stateOrCountry));
        ChatResponse response = chatClient.prompt(prompt).call().chatResponse();

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
        ChatResponse response = chatClient.prompt(prompt).call().chatResponse();

        return new Answer(response.getResult().getOutput().getContent());
    }




}
