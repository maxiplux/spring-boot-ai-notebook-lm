package app.quantun.summary.config.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Custom JSON serializer for Kafka messages.
 * This class is responsible for serializing Java objects into JSON messages.
 *
 * @param <T> the type of the serialized object
 */
@Slf4j
public class CustomJsonSerializer<T> implements Serializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Configures the serializer with the provided configurations.
     *
     * @param configs the configuration map
     * @param isKey   whether the serializer is for a key or value
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        log.info("Configuring CustomJsonSerializer with configs: {}", configs);
    }

    /**
     * Serializes the given Java object into a byte array.
     *
     * @param topic the topic to which the message will be sent
     * @param data  the Java object to serialize
     * @return the serialized byte array
     */
    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            log.warn("Null data received for serialization");
            return null;
        }
        try {
            log.info("Serializing data for topic: {}", topic);
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            log.error("Error serializing data for topic: {}", topic, e);
            throw new SerializationException("Error serializing data for topic " + topic, e);
        }
    }

    /**
     * Closes the serializer.
     */
    @Override
    public void close() {
        log.info("Closing CustomJsonSerializer");
    }
}
