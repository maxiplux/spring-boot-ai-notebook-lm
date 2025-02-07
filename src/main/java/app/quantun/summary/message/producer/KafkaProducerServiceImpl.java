package app.quantun.summary.message.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Kafka producer service implementation for sending messages to Kafka topics.
 * This class is responsible for producing and sending messages to the "bulk-data" topic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, Map<String, String>> kafkaTemplate;

    /**
     * Sends a batch of HashMap messages to the "bulk-data" topic.
     * Each message contains an ID, name, and value.
     */
    @Override
    public void sendHashMapMessage() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Map<String, String> payload = new HashMap<>();
            payload.put("id", String.valueOf(i));
            payload.put("name", "User" + i);
            payload.put("value", "Some data for user " + i);
            kafkaTemplate.send("bulk-data", payload);
            log.info("Message {} sent successfully", i);
        });
        log.info("Message sent successfully");
    }
}
