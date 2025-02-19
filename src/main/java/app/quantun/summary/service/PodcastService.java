package app.quantun.summary.service;

import app.quantun.summary.model.contract.dto.PodCast;
import app.quantun.summary.model.entity.Book;

public interface PodcastService {
    PodCast createScript(Book book);
}
