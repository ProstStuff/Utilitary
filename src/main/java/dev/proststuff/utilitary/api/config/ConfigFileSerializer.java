package dev.proststuff.utilitary.api.config;

import com.google.gson.*;
import dev.proststuff.utilitary.api.utility.SimpleIdentifier;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigFileSerializer implements JsonSerializer<ConfigFile>, JsonDeserializer<ConfigFile> {
    @Override
    public JsonElement serialize(ConfigFile src, Type typeOfSrc, JsonSerializationContext context) {
        return src.serialize();
    }

    @Override
    public ConfigFile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement configIdentity = jsonObject.get("identity");

            if (configIdentity != null && !configIdentity.isJsonNull()) {
                SimpleIdentifier identifier = SimpleIdentifier.parse(configIdentity.getAsString());
                AtomicReference<ConfigFile> atomicConfigFile = new AtomicReference<>();
                jsonObject.remove("identity");

                ConfigFile.getConfigFiles().computeIfPresent(identifier, (_, configFile) -> {
                    configFile.deserialize(json);
                    atomicConfigFile.set(configFile);
                    return configFile;
                });

                return atomicConfigFile.get();
            }
        }


        return null;
    }
}
