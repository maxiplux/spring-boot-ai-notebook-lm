package app.quantun.summary.model.dto;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "speak")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SsmlSpeak {

    @XmlAttribute
    private String version = "1.0";

    @XmlAttribute
    private String xmlns = "http://www.w3.org/2001/10/synthesis";

    @XmlAttribute(namespace = "http://www.w3.org/XML/1998/namespace")
    private String lang;

    @XmlElement(name = "voice")
    private List<SsmlVoice> voices = new ArrayList<>();
}
