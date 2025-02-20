package app.quantun.summary.util;

import app.quantun.summary.model.contract.dto.CustomMultipartFile;
import app.quantun.summary.model.contract.dto.SsmlSpeak;
import app.quantun.summary.model.contract.request.Base64FileUploadRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;


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

    public static MultipartFile base64ToMultipart(Base64FileUploadRequest file) throws IOException {
        // Extract metadata and decode

        String data = file.getBase64File();
        String mimeType = "application/pdf";
        byte[] bytes = Base64.getDecoder().decode(data);

        // Create temp file
        Path tempFile = Files.createTempFile("file", ".tmp");
        Files.write(tempFile, bytes);

        // Generate filename with extension
        String extension = switch (mimeType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "application/pdf" -> ".pdf";
            default -> throw new IllegalArgumentException("Unsupported MIME type");
        };

        return new CustomMultipartFile(
                bytes,
                file.getFileName() + extension,
                mimeType
        );
    }
}
