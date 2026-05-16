package dev.proststuff.utilitary.serialization;

import com.google.gson.*;
import net.minecraft.resources.Identifier;

import java.lang.reflect.Type;

public class ConfigFileSerializer implements JsonSerializer<ConfigFile>, JsonDeserializer<ConfigFile> {
    @Override
    public JsonElement serialize(ConfigFile src, Type typeOfSrc, JsonSerializationContext context) {
        return src.serialize(context);
    }

    @Override
    public ConfigFile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement configIdentity = jsonObject.get("identity");

            if (configIdentity != null && !configIdentity.isJsonNull()) {
                Identifier identifier = Identifier.parse(configIdentity.getAsString());
                jsonObject.remove("identity");
                if (ConfigFile.getConfigFiles().containsKey(identifier)) {
                    ConfigFile configFile = ConfigFile.getConfigFiles().get(identifier);
                    configFile.deserialize(json, context);
                    return configFile;
                }
            }
        }


        return null;
    }
}
