package app.quantun.summary.message.consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringJUnitConfig
@EmbeddedKafka(partitions = 1, topics = {"bulk-data"})
class MyKafkaConsumerTest {

    private MyKafkaConsumer myKafkaConsumer;
    private KafkaTemplate<String, Map<String, String>> kafkaTemplate;

    @BeforeEach
    public void setUp(EmbeddedKafkaBroker embeddedKafkaBroker) {
        myKafkaConsumer = Mockito.spy(new MyKafkaConsumerImpl());

        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        ProducerFactory<String, Map<String, String>> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    @Test
    void testListen() {
        Map<String, String> message = new HashMap<>();
        message.put("key", "value");

        kafkaTemplate.send("bulk-data", message);

        verify(myKafkaConsumer, timeout(5000).times(1)).processMessages(any());
    }
}
