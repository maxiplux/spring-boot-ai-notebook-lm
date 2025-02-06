package app.quantun.summary.service.impl;

import app.quantun.summary.model.dto.SsmlSpeak;
import app.quantun.summary.model.dto.SsmlVoice;
import app.quantun.summary.service.rest.TextToSpeechClient;
import app.quantun.summary.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeechServiceImpl {

    private final TextToSpeechClient ttsClient;
    @Value("${app.config.tts.save.path}")
    private String ttsSavePath;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${azure.tts.subscription-key}")
    private String apiKey;

    @Value("${azure.tts.server-url}")
    private String apiEndpoint;

    /**
     * Generates speech from text using the TextToSpeechClient.
     *
     * @return the status of the speech generation
     */
    public String generateSpeech() {
        SsmlSpeak speak = createSsmlSpeak();
        val data = this.ttsClient.synthesizeSpeech(Util.toSsml(speak));
        return saveAudioFile(data);
    }

    /**
     * Creates an SsmlSpeak object with predefined voices and text.
     *
     * @return the SsmlSpeak object
     */
    private SsmlSpeak createSsmlSpeak() {
        return SsmlSpeak.builder()
                .lang("en-US")
                .version("1.0")
                .xmlns("http://www.w3.org/2001/10/synthesis")
                .voices(
                        Arrays.asList(
                                SsmlVoice.builder()
                                        .name("en-US-AvaMultilingualNeural")
                                        .text("Before going any further—when we say first-time students, we mean the first time they meet you.\n" +
                                                "\n" +
                                                "Whether you’re an independent online tutor or work with a company, you’ll meet new enrollees in your classes.\n" +
                                                "\n" +
                                                "They may be first-time experimenters with online ESL learning. They may be first-time trials of your company’s or your private services.\n" +
                                                "\n" +
                                                "Or, they may be first-time participants in your class. Whichever the case may be, it will be the first time they meet you.")
                                        .build(),
                                SsmlVoice.builder()
                                        .name("en-US-AndrewMultilingualNeural")
                                        .text(" One of the most important and often neglected aspects of teaching is the element of courtesy.\n" +
                                                "\n" +
                                                "Students want to feel like they’re important and appreciated for their business.\n" +
                                                "\n" +
                                                "And even when they’re not actively thinking about those points, courtesy just makes sense.\n" +
                                                "\n" +
                                                "We’re not only in the teaching business, but also the customer service business.\n" +
                                                "\n" +
                                                "Teaching English as a language naturally makes us a service-oriented industry. Creating satisfied customers is an important part of any service-oriented business.")
                                        .build()
                        )
                )
                .build();
    }

    /**
     * Saves the audio file to the specified path.
     *
     * @param data the audio data
     * @return the status of the file saving
     */
    private String saveAudioFile(byte[] data) {
        try {
            Files.write(Paths.get(ttsSavePath + "audio.mp3"), data);
            return "OK";
        } catch (IOException e) {
            log.error("Error saving audio file {}", e.getMessage());
        }
        return "Error";
    }

    /**
     * Converts text to speech using the Azure TTS API.
     *
     * @param ssml the SSML string
     * @param outputFilePath the output file path
     * @return the status of the text-to-speech conversion
     */
    public String convertTextToSpeech(String ssml, String outputFilePath) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/ssml+xml"));
        headers.set("X-Microsoft-OutputFormat", "audio-16khz-128kbitrate-mono-mp3");
        headers.set("Ocp-Apim-Subscription-Key", apiKey);
        headers.set("User-Agent", "Spring Boot Application");

        HttpEntity<String> request = new HttpEntity<>(ssml, headers);

        try {
            ResponseEntity<byte[]> response = restTemplate.postForEntity(
                    apiEndpoint + "/cognitiveservices/v1",
                    request,
                    byte[].class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Files.write(Paths.get(outputFilePath + "audio.mp3"), response.getBody());
                log.info("Audio file saved successfully: " + outputFilePath);
                return "OK";
            } else {
                log.error("Error: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error converting text to speech: {}", e.getMessage());
        }
        return "Error";
    }
}
