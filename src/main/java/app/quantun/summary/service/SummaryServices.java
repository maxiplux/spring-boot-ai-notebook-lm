package app.quantun.summary.service;

import app.quantun.summary.model.contract.dto.BookSummary;
import app.quantun.summary.model.entity.Book;

public interface SummaryServices {
    public BookSummary summarizeBook(Book bookText) ;
}
