package app.quantun.summary.message.consumer;

import app.quantun.summary.model.contract.MessagePayload;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;
import java.util.Map;

public interface MyKafkaConsumer {
    @KafkaListener(topics = "bulk-data", groupId = "my-group")
    void listen(List<MessagePayload> messages);
}
