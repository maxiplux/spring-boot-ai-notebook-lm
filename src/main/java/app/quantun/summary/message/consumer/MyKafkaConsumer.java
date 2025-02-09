package app.quantun.summary.message.consumer;

import app.quantun.summary.model.contract.MessagePayload;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;
import java.util.Map;

public interface MyKafkaConsumer {
    void processMessages(List<MessagePayload> messages);
}
