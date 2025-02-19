package app.quantun.summary.message.consumer;

import app.quantun.summary.model.contract.message.BookFilePayload;

import java.util.List;

public interface SummaryBookConsumer {
    void processMessages(List<BookFilePayload> messages);
}
