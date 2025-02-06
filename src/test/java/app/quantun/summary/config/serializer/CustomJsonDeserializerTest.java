package app.quantun.summary.config.serializer;

import org.apache.kafka.common.errors.SerializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CustomJsonDeserializerTest {

    private CustomJsonDeserializer<TestObject> deserializer;

    @BeforeEach
    void setUp() {
        deserializer = new CustomJsonDeserializer<>();
        Map<String, Object> configs = new HashMap<>();
        configs.put("spring.json.value.default.type", "app.quantun.summary.config.serializer.CustomJsonDeserializerTest$TestObject");
        deserializer.configure(configs, false);
    }

    @Test
    void testDeserialize() {
        String json = "{\"name\":\"Test Name\",\"value\":\"Test Value\"}";
        byte[] data = json.getBytes();
        TestObject result = deserializer.deserialize("test-topic", data);
        assertNotNull(result);
        assertEquals("Test Name", result.getName());
        assertEquals("Test Value", result.getValue());
    }

    @Test
    void testDeserializeNullData() {
        TestObject result = deserializer.deserialize("test-topic", null);
        assertNull(result);
    }

    @Test
    void testDeserializeInvalidData() {
        byte[] data = "invalid json".getBytes();
        assertThrows(SerializationException.class, () -> deserializer.deserialize("test-topic", data));
    }

    @Test
    void testConfigureWithInvalidClass() {
        CustomJsonDeserializer<TestObject> invalidDeserializer = new CustomJsonDeserializer<>();
        Map<String, Object> configs = new HashMap<>();
        configs.put("spring.json.value.default.type", "invalid.class.Name");
        assertThrows(SerializationException.class, () -> invalidDeserializer.configure(configs, false));
    }

    static class TestObject {
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
