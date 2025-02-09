package app.quantun.summary.message.producer;

import app.quantun.summary.model.contract.MessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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

  /**
   * Sends a batch of HashMap messages to the "bulk-data" topic.
   * Each message contains an ID, name, and value.
   */
  @Override
  public void sendHashMapMessage() {
    IntStream.rangeClosed(1, 10)
        .forEach(
            i -> {
                MessagePayload messagePayload = new MessagePayload( String.valueOf(i), "User" + i, "Some data for user " + i);

              kafkaTemplate.send("bulk-data", messagePayload);
              log.info("Message {} sent successfully", i);
            });
    log.info("Message sent successfully");
  }
}
