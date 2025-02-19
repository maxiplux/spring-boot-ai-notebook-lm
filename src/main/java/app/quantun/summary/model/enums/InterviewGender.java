package app.quantun.summary.model.enums;

import lombok.Getter;

@Getter
public enum InterviewGender {
    FEMALE("Female"),
    MALE("Male");

    private final String gender;

    InterviewGender(String gender) {
        this.gender = gender;
    }


}
