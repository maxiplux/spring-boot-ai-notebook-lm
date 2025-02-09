package app.quantun.summary.repository;

import app.quantun.summary.model.entity.SummaryBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryBookRepository extends JpaRepository<SummaryBook, Long> {
}