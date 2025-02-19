package app.quantun.summary.service.impl;

import app.quantun.summary.message.producer.SummaryBookProducerService;
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
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PdfServicesImplTest {

    @Mock
    private SummaryBookProducerService summaryBookProducerService;

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
    void testStorePdfFile() {
        MultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test content".getBytes());
        String expectedFileName = "test.pdf";
        when(fileStorageService.storePdfFile(file)).thenReturn(expectedFileName);

        String result = pdfServices.storePdfFile(file);

        assertEquals(expectedFileName, result);
        verify(fileStorageService).storePdfFile(file);
        //verify(summaryBookProducerService).sendHashMapMessage();
    }
}
