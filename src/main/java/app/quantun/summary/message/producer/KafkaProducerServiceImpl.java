package app.quantun.summary.message.producer;

import app.quantun.summary.model.contract.MessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * Kafka producer service implementation for sending messages to Kafka topics.
 * This class is responsible for producing and sending messages to the "bulk-data" topic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

  private final KafkaTemplate<Object, MessagePayload> kafkaTemplate;

    // Externalize the topic name for better configurability
    @Value("${kafka.topic.bulk-data}")
    private String bulkDataTopic;


    /**
     * Sends a batch of HashMap messages to the "bulk-data" topic.
     * Each message contains an ID, name, and value.
     */
    @Override
    public void sendHashMapMessage() {
        IntStream.rangeClosed(1, 10)
                .forEach(i -> {
                    MessagePayload messagePayload = new MessagePayload(
                            String.valueOf(i),
                            "User" + i,
                            "Some data for user " + i
                    );

                    CompletableFuture<SendResult<Object, MessagePayload>> future = kafkaTemplate.send(bulkDataTopic, messagePayload).toCompletableFuture();

                    future.thenAccept(result ->
                                    log.info("Successfully sent message {} with payload: {}", i, messagePayload))


                            .exceptionally(ex -> {
                                log.error("Failed to send message {} with payload: {}. Exception: {}", i, messagePayload, ex.getMessage());
                                return null;
                            });
                });
    }

}
