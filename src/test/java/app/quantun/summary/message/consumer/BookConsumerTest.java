package app.quantun.summary.message.consumer;

import app.quantun.summary.repository.BookRepository;
import app.quantun.summary.service.PodcastService;
import app.quantun.summary.service.SummaryServices;
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
class BookConsumerTest {

    private SummaryBookConsumer summaryBookConsumer;
    private KafkaTemplate<String, Map<String, String>> kafkaTemplate;

    @BeforeEach
    public void setUp(EmbeddedKafkaBroker embeddedKafkaBroker) {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);

        BookRepository repository = Mockito.mock(BookRepository.class);
        PodcastService podcastService = Mockito.mock(PodcastService.class);

        SummaryServices summaryServices = Mockito.mock(SummaryServices.class);

        summaryBookConsumer = Mockito.spy(new SummaryBookConsumerImpl(repository, summaryServices, podcastService));

        ProducerFactory<String, Map<String, String>> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        KafkaTemplate<String, Map<String, String>> kafkaTemplate = new KafkaTemplate<>(producerFactory);

    }

    @Test
    void testListen() {
        Map<String, String> message = new HashMap<>();
        message.put("key", "value");

        kafkaTemplate.send("bulk-data", message);

        verify(summaryBookConsumer, timeout(5000).times(1)).processMessages(any());
    }
}
