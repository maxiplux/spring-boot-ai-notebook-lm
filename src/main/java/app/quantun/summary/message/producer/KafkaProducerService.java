package app.quantun.summary.message.producer;

import app.quantun.summary.model.contract.message.BookFilePayload;

public interface KafkaProducerService {


  void sendBookToBeProcessed(BookFilePayload message);
}
