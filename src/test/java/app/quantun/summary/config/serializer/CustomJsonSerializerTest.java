package app.quantun.summary.config.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CustomJsonSerializerTest {

    private CustomJsonSerializer<TestObject> serializer;

    @BeforeEach
    void setUp() {
        serializer = new CustomJsonSerializer<>();
        Map<String, Object> configs = new HashMap<>();
        serializer.configure(configs, false);
    }

    @Test
    void testSerialize() {
        TestObject testObject = new TestObject("Test Name", "Test Value");
        byte[] data = serializer.serialize("test-topic", testObject);
        assertNotNull(data);
        assertTrue(data.length > 0);
    }

    @Test
    void testSerializeNullData() {
        byte[] data = serializer.serialize("test-topic", null);
        assertNull(data);
    }

    @Test
    void testSerializeInvalidData() {
        TestObject invalidObject = new TestObject(null, null);
        assertThrows(SerializationException.class, () -> serializer.serialize("test-topic", invalidObject));
    }

    static class TestObject {
        private String name;
        private String value;

        public TestObject(String name, String value) {
            this.name = name;
            this.value = value;
        }

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
