package app.quantun.summary.service.rest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "/cognitiveservices/v1")
public interface TextToSpeechClient {

    @PostExchange(

    )
    byte[] synthesizeSpeech(@RequestBody String ssml);
}
