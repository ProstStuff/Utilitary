package dev.proststuff.utilitary.api.config.impl;

import com.google.gson.JsonElement;

public interface ConfigSerializable {
    String getName();
    JsonElement serialize();
    void deserialize(JsonElement jsonElement);
}
