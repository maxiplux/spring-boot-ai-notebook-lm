package app.quantun.summary.model.entity;

import app.quantun.summary.model.enums.InterviewGender;
import app.quantun.summary.model.enums.InterviewLanguage;
import app.quantun.summary.model.enums.InterviewName;
import app.quantun.summary.model.enums.InterviewRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Script implements Comparable<Script> {

    private Integer position;

    private InterviewGender gender;

    private InterviewRole role;

    private InterviewLanguage language;

    private InterviewName name;


    private String text;


    @Override
    public int compareTo(Script o) {
        return this.position - o.position;
    }
}
