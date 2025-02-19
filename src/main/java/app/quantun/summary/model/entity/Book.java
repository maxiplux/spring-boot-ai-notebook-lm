package app.quantun.summary.model.entity;

import app.quantun.summary.model.contract.dto.BookSummary;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(indexes = {
        @Index(name = "idx_summarybook_uuid", columnList = "uuid")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;
    private String name;
    private String path;
    @Column(columnDefinition = "TEXT")
    private String uuid;

    @Type(io.hypersistence.utils.hibernate.type.json.JsonType.class)
    @Column(columnDefinition = "jsonb")
    private BookSummary textSummary;

    @ManyToOne(fetch = FetchType.LAZY)
    private PodCast podCast;


}
