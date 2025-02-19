package app.quantun.summary.message.consumer;

import app.quantun.summary.model.contract.message.BookFilePayload;
import app.quantun.summary.model.entity.Book;
import app.quantun.summary.repository.BookRepository;
import app.quantun.summary.service.PodcastService;
import app.quantun.summary.service.SummaryServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Kafka consumer service for processing messages from the "bulk-data" topic.
 * This class listens to Kafka messages and logs the received messages.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SummaryBookConsumerImpl implements SummaryBookConsumer {

    private final BookRepository summaryBookRepository;

    private final SummaryServices summaryServices;

    private final PodcastService podCastService;


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
    public void listen(List<BookFilePayload> messages, Acknowledgment ack) {
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
    public void processMessages(List<BookFilePayload> messages) {
        // Your processing logic here
        for (BookFilePayload message : messages) {
            this.summaryBookRepository.findById(message.id()).ifPresentOrElse(
                    book ->
                    {
                        log.info("Book with id {} already exists in the database", message.id());

                        this.makeSummary(book);
                        this.makePodcast(book);
                        this.saveChanges(book);


                    },
                    () ->
                    {
                        log.warn("Book with id {} does not exist in the database. Saving it...", message.id());

                    }
            );

            log.info("Processing message: {}", message);
        }

    }

    private void saveChanges(Book book) {
        try {
            log.info("Saving changes for book with id: {}", book.getId());
            this.summaryBookRepository.save(book);
            log.info("Successfully saved changes for book with id: {}", book.getId());
        } catch (Exception e) {
            log.error("Failed to save changes for book with id: {}. Error: {}", book.getId(), e.getMessage());

        }
    }


    private void makePodcast(Book book) {
        val podCast = this.podCastService.createScript(book);
        book.setPodCast(podCast);

    }

    private void makeSummary(Book book) {
        val summary = summaryServices.summarizeBook(book);
        book.setTextSummary(summary);

    }

}
