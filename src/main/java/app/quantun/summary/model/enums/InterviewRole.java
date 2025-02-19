package app.quantun.summary.model.enums;

import lombok.Getter;

@Getter
public enum InterviewRole {


    GUEST("guest"),
    HOST("host");

    private final String role;

    InterviewRole(String role) {
        this.role = role;
    }


}
