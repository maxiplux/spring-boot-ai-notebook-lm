package app.quantun.summary.message.consumer;

import app.quantun.summary.model.contract.MessagePayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Kafka consumer service for processing messages from the "bulk-data" topic.
 * This class listens to Kafka messages and logs the received messages.
 */
@Service
@Slf4j
public class MyKafkaConsumerImpl implements MyKafkaConsumer {


  /**
   * Process incoming messages from the "bulk-data" topic.
   *
   * @param messages        List of messages to process.
   * @param ack             Acknowledgment object for manual offset management.
   * @param record          The ConsumerRecord for logging metadata.
   */
  @KafkaListener(
          topics = "bulk-data",
          groupId = "my-group",

          containerFactory = "kafkaListenerContainerFactory"
  )
  @Retryable(
          value = Exception.class,
          maxAttempts = 3,
          backoff = @Backoff(delay = 2000)
  )
  public void listen(List<MessagePayload> messages, Acknowledgment ack) {
    try {
      log.info("Received batch of {} messages", messages.size());

      processMessages(messages);
      // Manually acknowledge after processing
      ack.acknowledge();
      log.info("Successfully processed and acknowledged messages.");

    } catch (Exception e) {
      log.error("Error processing messages: {} with exception: {}", messages, e.getMessage());
      // Optionally, you might want to send these messages to a dead letter queue or perform some cleanup
      // Re-throw exception to trigger retry as defined by @Retryable
      throw e;
    }
  }

  /**
   * Business logic for processing messages.
   *
   * @param messages List of MessagePayload to process.
   */
  public void processMessages(List<MessagePayload> messages) {
    // Your processing logic here
    for (MessagePayload message : messages) {
      log.info("Processing message: {}", message);
    }

  }

}
