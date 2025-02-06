package app.quantun.summary;

import app.quantun.summary.model.dto.SsmlSpeak;
import app.quantun.summary.model.dto.SsmlVoice;
import app.quantun.summary.util.Util;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;


public class UtilTest {
    @Test
    public void xmlSmlTest() throws JAXBException {


        SsmlSpeak speak = SsmlSpeak.builder()
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


        assertNotNull(Util.toSsml(speak));
        ;
    }

}
