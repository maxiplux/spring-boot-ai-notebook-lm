package app.quantun.summary.model.entity;

import app.quantun.summary.model.enums.InterviewGender;
import app.quantun.summary.model.enums.InterviewLanguage;
import app.quantun.summary.model.enums.InterviewRole;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Script implements Comparable<Script> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    private Integer position;

    private InterviewGender gender;

    private InterviewRole role;

    private InterviewLanguage language;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String text;


    @Override
    public int compareTo(Script o) {
        return this.position - o.position;
    }
}
