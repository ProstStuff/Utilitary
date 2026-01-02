package dev.proststuff.utilitary.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConfigGroup extends ConfigBase {
    protected final List<ConfigBase> children = new ArrayList<>();

    public ConfigGroup(Identifier identifier) {
        super(identifier);
    }

    public List<ConfigBase> getChildren() {
        return children;
    }

    public void add(ConfigBase entry) {
        children.add(entry);
    }

    @Override
    public JsonElement encode() {
        JsonObject encoded = new JsonObject();

        for (ConfigBase entry : children) {
            @Nullable JsonElement value = entry.encode();
            if (value != null) {
                encoded.add(entry.getIdentifier().getPath(), entry.encode());
            }
        }

        return encoded;
    }

    @Override
    public void decode(JsonElement element) {
        if (!element.isJsonObject()) return;
        JsonObject toDecode = element.getAsJsonObject();

        for (ConfigBase entry : children) {
            String name = entry.getIdentifier().getPath();
            JsonElement child = toDecode.get(name);
            entry.decode(child);
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (ConfigBase child : children) {
            str.append("[").append(child.toString()).append("]");
        }

        return "ConfigGroup${" + str + "}" ;
    }
}
