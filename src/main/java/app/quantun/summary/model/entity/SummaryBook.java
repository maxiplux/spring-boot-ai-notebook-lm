package app.quantun.summary.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(indexes = {
        @Index(name = "idx_summarybook_uuid", columnList = "uuid")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SummaryBook {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;
    private String name;
    private String path;
    @Column(columnDefinition = "TEXT")
    // add index

    private String uuid;


}
