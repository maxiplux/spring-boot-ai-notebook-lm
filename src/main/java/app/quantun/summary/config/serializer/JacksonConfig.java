package app.quantun.summary.config.serializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.protobuf.Message;
import com.google.protobuf.UnknownFieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Optionally, ignore unknown properties globally
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        // Register the mixin for UnknownFieldSet
        mapper.addMixIn(UnknownFieldSet.class, UnknownFieldSetMixin.class);
        return mapper;
    }


    @Bean
    public SimpleModule protoModule() {
        SimpleModule module = new SimpleModule();
        module.setMixInAnnotation(Message.class, ProtoMessageMixin.class);
        return module;
    }

}

