package app.quantun.summary.config.serializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.protobuf.Message;
import com.google.protobuf.UnknownFieldSet;
import org.springframework.boot.jackson.JsonMixin;

@JsonMixin(Message.class)
public abstract class ProtoMessageMixin {
    @JsonIgnore
    public abstract UnknownFieldSet getUnknownFields();

    @JsonIgnore
    public abstract UnknownFieldSet getDefaultInstanceForType();
}
