package app.quantun.summary.config.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Custom JSON deserializer for Kafka messages.
 * This class is responsible for deserializing JSON messages into Java objects.
 *
 * @param <T> the type of the deserialized object
 */
@Slf4j
public class CustomJsonDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Class<T> targetClass;

    /**
     * Configures the deserializer with the provided configurations.
     *
     * @param configs the configuration map
     * @param isKey   whether the deserializer is for a key or value
     */
    @Override
    @SuppressWarnings("unchecked")
    public void configure(Map<String, ?> configs, boolean isKey) {
        log.info("Configuring CustomJsonDeserializer with configs: {}", configs);
        Object clazz = configs.get("spring.json.value.default.type");
        if (clazz instanceof String) {
            try {
                targetClass = (Class<T>) Class.forName((String) clazz);
                log.info("Deserialization target class set to: {}", targetClass.getName());
            } catch (ClassNotFoundException e) {
                log.error("Class not found for deserialization", e);
                throw new SerializationException("Class not found", e);
            }
        }
    }

    /**
     * Deserializes the given byte array into a Java object.
     *
     * @param topic the topic from which the message was received
     * @param data  the byte array to deserialize
     * @return the deserialized object
     */
    @Override
    public T deserialize(String topic, byte[] data) {
        log.info("Deserializing message from topic: {}", topic);
        if (data == null) {
            log.warn("Received null data for deserialization");
            return null;
        }
        if (targetClass == null) {
            log.error("Target class is not set for deserialization");
            throw new SerializationException("Target class is not set for deserialization");
        }
        try {
            T result = objectMapper.readValue(data, targetClass);
            log.info("Deserialization successful for message: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error deserializing JSON message", e);
            throw new SerializationException("Error deserializing JSON message", e);
        }
    }

    /**
     * Closes the deserializer.
     */
    @Override
    public void close() {
        log.info("Closing CustomJsonDeserializer");
    }
}
