package app.quantun.summary.message.producer;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;

@SpringJUnitConfig
@EmbeddedKafka(partitions = 1, topics = {"bulk-data"})
@SpringBootTest
public class BookProducerServiceImplTest {

    @Autowired
    private SummaryBookProducerService summaryBookProducerService;

    @Autowired
    private KafkaTemplate<String, Map<String, String>> kafkaTemplate;

    @BeforeEach
    public void setUp(EmbeddedKafkaBroker embeddedKafkaBroker) {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        ProducerFactory<String, Map<String, String>> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }


}
