package app.quantun.summary.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.TreeSet;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PodCast {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String title;
    private Long duration;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private TreeSet<Script> scripts;


}
