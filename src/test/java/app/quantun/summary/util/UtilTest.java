package app.quantun.summary.util;

import app.quantun.summary.model.dto.SsmlSpeak;
import app.quantun.summary.model.dto.SsmlVoice;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilTest {

    @Test
    public void testToSsml() {
        SsmlSpeak speak = SsmlSpeak.builder()
                .lang("en-US")
                .version("1.0")
                .xmlns("http://www.w3.org/2001/10/synthesis")
                .voices(Collections.singletonList(
                        SsmlVoice.builder()
                                .name("en-US-AvaMultilingualNeural")
                                .text("Hello, this is a test.")
                                .build()
                ))
                .build();

        String ssml = Util.toSsml(speak);
        assertTrue(ssml.contains("Hello, this is a test."));
        assertTrue(ssml.contains("en-US-AvaMultilingualNeural"));
    }

    @Test
    public void testToSsmlWithEmptySpeak() {
        SsmlSpeak speak = new SsmlSpeak();
        String ssml = Util.toSsml(speak);
        assertTrue(ssml.isEmpty());
    }

    @Test
    public void testToSsmlWithNullSpeak() {
        String ssml = Util.toSsml(null);
        assertTrue(ssml.isEmpty());
    }
}
