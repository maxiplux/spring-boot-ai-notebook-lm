package app.quantun.summary.config.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class CustomJsonDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Class<T> targetClass;

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

    @Override
    public T deserialize(String topic, byte[] data) {
        log.info("Deserializing message from topic: {}", topic);
        if (data == null) {
            log.warn("Received null data for deserialization");
            return null;
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

    @Override
    public void close() {
        log.info("Closing CustomJsonDeserializer");
    }
}