package app.quantun.summary.config.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class CustomJsonSerializer<T> implements Serializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        log.info("Configuring CustomJsonSerializer with configs: {}", configs);
    }

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

    @Override
    public void close() {
        log.info("Closing CustomJsonSerializer");
    }
}