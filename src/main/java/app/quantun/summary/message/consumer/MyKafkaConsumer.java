package app.quantun.summary.message.consumer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MyKafkaConsumer {

    @KafkaListener(topics = "bulk-data", groupId = "my-group")
    public void listen(List<Map<String, String>> messages) {
        log.info("Received batch of " + messages.size() + " messages:");
        messages.forEach(message -> log.info("  " + message));

    }
}
