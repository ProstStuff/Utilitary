package dev.proststuff.utilitary.config;

import com.google.gson.JsonElement;
import net.minecraft.util.Identifier;

public abstract class ConfigBase {
    protected Identifier identifier;

    public ConfigBase(Identifier identifier) {
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public abstract JsonElement encode();
    public abstract void decode(JsonElement element);

    public boolean is(ConfigBase other) {
        return other.identifier.getNamespace().equals(this.identifier.getNamespace());
    }
}