package app.quantun.summary.service.impl;

import app.quantun.summary.message.producer.KafkaProducerService;
import app.quantun.summary.model.dto.TableIndexContent;
import app.quantun.summary.model.request.Answer;
import app.quantun.summary.service.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PdfServicesImplTest {

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    @Qualifier("openAiChatClient")
    private ChatClient openAiChatClient;

    @Mock
    @Qualifier("geminiChatClient")
    private ChatClient geminiChatClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private PdfServicesImpl pdfServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBookTableOfContentPages() {
        String message = "Extract the Content for this book";
        TableIndexContent expectedContent = new TableIndexContent();
        when(geminiChatClient.prompt(any()).user(any())).thenReturn(() -> () -> "response");
        when(objectMapper.convertValue(anyString(), eq(TableIndexContent.class))).thenReturn(expectedContent);

        TableIndexContent result = pdfServices.getBookTableOfContentPages(message);

        assertEquals(expectedContent, result);
        verify(geminiChatClient).prompt(any());
    }

    @Test
    void testGetResponse() {
        String message = "Hello";
        String expectedResponse = "Hi there!";
        when(openAiChatClient.prompt(any()).call().content()).thenReturn(expectedResponse);

        String result = pdfServices.getResponse(message);

        assertEquals(expectedResponse, result);
        verify(openAiChatClient).prompt(any());
    }

    @Test
    void testGetSimpleAnswerFromRandomQuestionString() {
        String message = "What is the capital of France?";
        String expectedAnswer = "Paris";
        when(openAiChatClient.prompt(any()).call().content()).thenReturn(expectedAnswer);

        Answer result = pdfServices.getSimpleAnswerFromRandomQuestionString(message);

        assertEquals(expectedAnswer, result.getAnswer());
        verify(openAiChatClient).prompt(any());
    }

    @Test
    void testGetCapital() throws IOException {
        String stateOrCountry = "France";
        String expectedCapital = "Paris";
        when(openAiChatClient.prompt(any()).call().chatResponse().getResult().getOutput().getContent()).thenReturn("{\"answer\":\"Paris\"}");
        when(objectMapper.readTree(anyString()).get("answer").asText()).thenReturn(expectedCapital);

        Answer result = pdfServices.getCapital(stateOrCountry);

        assertEquals(expectedCapital, result.getAnswer());
        verify(openAiChatClient).prompt(any());
    }

    @Test
    void testGetCapitalWithInfo() {
        String stateOrCountry = "France";
        String expectedInfo = "Paris is the capital of France.";
        when(openAiChatClient.prompt(any()).call().chatResponse().getResult().getOutput().getContent()).thenReturn(expectedInfo);

        Answer result = pdfServices.getCapitalWithInfo(stateOrCountry);

        assertEquals(expectedInfo, result.getAnswer());
        verify(openAiChatClient).prompt(any());
    }

    @Test
    void testStorePdfFile() {
        MultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test content".getBytes());
        String expectedFileName = "test.pdf";
        when(fileStorageService.storePdfFile(file)).thenReturn(expectedFileName);

        String result = pdfServices.storePdfFile(file);

        assertEquals(expectedFileName, result);
        verify(fileStorageService).storePdfFile(file);
        verify(kafkaProducerService).sendHashMapMessage();
    }
}
