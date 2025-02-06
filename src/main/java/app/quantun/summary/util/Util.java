package app.quantun.summary.util;

import app.quantun.summary.model.dto.SsmlSpeak;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;

@Slf4j
public class Util {


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
