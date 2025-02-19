package app.quantun.summary.model.enums;

import lombok.Getter;

@Getter
public enum InterviewLanguage {
    EN_US("en-US"),
    ES_US("es-US"),
    ES_MX("es-MX");

    private final String languageTag;

    InterviewLanguage(String languageTag) {
        this.languageTag = languageTag;
    }

}
