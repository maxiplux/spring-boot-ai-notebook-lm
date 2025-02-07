package app.quantun.summary.util;

import app.quantun.summary.model.dto.SsmlSpeak;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;

/**
 * Utility class for SSML (Speech Synthesis Markup Language) operations.
 * This class provides methods to convert SsmlSpeak objects to SSML strings.
 */
@Slf4j
public class Util {

    /**
     * Converts an SsmlSpeak object to an SSML string.
     *
     * @param speak the SsmlSpeak object to convert
     * @return the SSML string representation of the SsmlSpeak object
     */
    public static String toSsml(SsmlSpeak speak) {
        try {
            log.info("Ssml Start ");
            JAXBContext context = JAXBContext.newInstance(SsmlSpeak.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter writer = new StringWriter();
            marshaller.marshal(speak, writer);
            log.info("Ssml End : {}", "OK");
            return writer.toString();
        } catch (JAXBException e) {
            log.error("Error while marshalling ssml {}", e.getMessage());
        }
        return "";
    }
}
