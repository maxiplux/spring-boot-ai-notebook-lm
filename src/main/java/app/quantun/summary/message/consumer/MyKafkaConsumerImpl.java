package app.quantun.summary.message.consumer;

import app.quantun.summary.model.contract.MessagePayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
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
   * Listens to messages from the "bulk-data" topic and logs the received messages.
   *
   * @param messages the list of messages received from the topic
   */
  @KafkaListener(topics = "bulk-data", groupId = "my-group")
  @Override
  public void listen(List<MessagePayload> messages) {
    log.info("Received batch of " + messages.size() + " messages:");

    messages.forEach(message -> log.info("  " + message));
  }
}
