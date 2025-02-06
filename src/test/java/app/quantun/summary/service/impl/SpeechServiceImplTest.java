package app.quantun.summary.service.impl;

import app.quantun.summary.service.rest.TextToSpeechClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SpeechServiceImplTest {

    @Mock
    private TextToSpeechClient ttsClient;

    @Mock
    private RestTemplate restTemplate;

    @Value("${app.config.tts.save.path}")
    private String ttsSavePath;

    @InjectMocks
    private SpeechServiceImpl speechService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void generateSpeech() throws IOException {
        byte[] mockData = "audio data".getBytes();
        when(ttsClient.synthesizeSpeech(any())).thenReturn(mockData);

        String result = speechService.generateSpeech();

        assertEquals("OK", result);
        verify(ttsClient).synthesizeSpeech(any());
        Files.deleteIfExists(Paths.get(ttsSavePath + "audio.mp3"));
    }

    @Test
    void convertTextToSpeech() throws IOException {
        String ssml = "<speak>Test</speak>";
        String outputFilePath = ttsSavePath + "test_audio.mp3";
        byte[] mockData = "audio data".getBytes();
        when(restTemplate.postForEntity(anyString(), any(), eq(byte[].class))).thenReturn(new ResponseEntity<>(mockData, HttpStatus.OK));

        String result = speechService.convertTextToSpeech(ssml, outputFilePath);

        assertEquals("OK", result);
        verify(restTemplate).postForEntity(anyString(), any(), eq(byte[].class));
        Files.deleteIfExists(Paths.get(outputFilePath));
    }
}
