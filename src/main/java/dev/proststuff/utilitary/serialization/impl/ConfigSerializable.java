package dev.proststuff.utilitary.serialization.impl;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

public interface ConfigSerializable {
    String getName();
    JsonElement serialize(JsonSerializationContext context);
    void deserialize(JsonElement jsonElement, JsonDeserializationContext context);
}
