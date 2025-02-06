package app.quantun.summary.message.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringJUnitConfig
@EmbeddedKafka(partitions = 1, topics = { "bulk-data" })
@SpringBootTest
public class KafkaProducerServiceImplTest {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private KafkaTemplate<String, Map<String, String>> kafkaTemplate;

    @BeforeEach
    public void setUp(EmbeddedKafkaBroker embeddedKafkaBroker) {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        ProducerFactory<String, Map<String, String>> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    @Test
    public void testSendHashMapMessage() {
        kafkaProducerService = Mockito.spy(new KafkaProducerServiceImpl(kafkaTemplate));

        kafkaProducerService.sendHashMapMessage();

        verify(kafkaProducerService, times(1)).sendHashMapMessage();
    }
}
