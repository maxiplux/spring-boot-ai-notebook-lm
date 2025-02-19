package app.quantun.summary.config.serializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.protobuf.UnknownFieldSet;

public abstract class UnknownFieldSetMixin {
    @JsonIgnore
    abstract UnknownFieldSet getDefaultInstanceForType();
}
