package app.quantun.summary.model.enums;

import lombok.Getter;

@Getter
public enum InterviewName {
    SARAH("Sarah"),
    MICHAEL("Michael");

    private final String name;

    InterviewName(String name) {
        this.name = name;
    }


}
