package app.quantun.summary.message.producer;

import app.quantun.summary.model.contract.message.BookFilePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka producer service implementation for sending messages to Kafka topics.
 * This class is responsible for producing and sending messages to the "bulk-data" topic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryBookProducerServiceImpl implements SummaryBookProducerService {

    private final KafkaTemplate<Object, BookFilePayload> kafkaTemplate;

    // Externalize the topic name for better configurability
    @Value("${kafka.topic.bulk-data}")
    private String bulkDataTopic;


    @Override
    public void sendBookToBeProcessed(BookFilePayload message) {
        CompletableFuture<SendResult<Object, BookFilePayload>> future = kafkaTemplate.send(bulkDataTopic, message).toCompletableFuture();

        future.thenAccept(result ->
                        log.info("Successfully sent message with payload: {}", message))

                .exceptionally(ex -> {
                    log.error("Failed to send message with payload: {}. Exception: {}", message, ex.getMessage());
                    return null;
                });

    }

}
