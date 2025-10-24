package dev.proststuff.utilitary.config;

import com.google.gson.*;
import dev.proststuff.utilitary.Utilitary;
import dev.proststuff.utilitary.utility.IFancyLogging;
import dev.proststuff.utilitary.utility.config.ConfigEnvironment;
import dev.proststuff.utilitary.utility.config.ConfigFileWatcher;
import dev.proststuff.utilitary.utility.config.gson.ConfigValueAdapter;
import net.minecraft.util.WorldSavePath;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigFile extends ConfigBase<ConfigOption> {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ConfigValue.class, new ConfigValueAdapter<>())
            .setPrettyPrinting()
            .create();
    private static final Object configLock = new Object();

    protected final ConfigOption root;
    protected final ConfigEnvironment environment;

    public ConfigFile(String name, ConfigEnvironment configEnvironment) {
        super(name);
        this.environment = configEnvironment;
        this.root = new ConfigOption(name);
    }

    public Path getFilePath() {
        if (manager == null) {
            throw new IllegalStateException("ConfigManager is null.");
        }

        if (this.environment == ConfigEnvironment.SERVER) {
            if (Utilitary.SERVER == null) throw new IllegalStateException("Unable to fetch MinecraftServer for " + name + " config");
            return Utilitary.SERVER.getSavePath(WorldSavePath.DATAPACKS).getParent().resolve("serverconfig").resolve(manager.NAME).resolve(name + ".json");
        }

        return Path.of("config", manager.NAME, getName() + ".json");
    }

    @Override
    public void setConfigManager(ConfigManager configManager) {
        super.setConfigManager(configManager);
        root.setConfigManager(getConfigManager());
    }

    @Override
    public ConfigOption get() {return root;}
    @Override
    public ConfigOption getDefault() {return root;}
    public ConfigEnvironment getEnvironment() {return environment;}

    public ConfigFile add(ConfigBase<?> config) {
        root.add(config);
        return this;
    }

    @Override
    public JsonElement encode() {
        return root.encode();
    }

    @Override
    public void decode(JsonElement element) {
        root.decode(element);
    }

    public void write() {
        manager.info("Writing {}.json file", name);
        if (root.getEntries().isEmpty()) {
            manager.warn(IFancyLogging.LogType.SUB, "Empty config entries. {}.json will not be created.", name);
            return;
        }

        Path configFilePath = getFilePath();
        createDirectory(configFilePath.getParent());

        try (Writer writer = Files.newBufferedWriter(configFilePath)) {
            JsonObject encoded = encode().getAsJsonObject();
            GSON.toJson(encoded, writer);
        } catch (IOException e) {
            manager.error(e.getMessage());
        }
    }

    public void read() {
        manager.info("Reading {}.json file", name);
        synchronized (configLock) {
            Path configFilePath = getFilePath();
            boolean exists = Files.exists(getFilePath());
            boolean shouldWrite = false;

            if (exists) {
                try (Reader reader = Files.newBufferedReader(configFilePath)) {
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    decode(json);
                } catch (IOException e) {
                    manager.error(e.getMessage());
                    shouldWrite = true;
                }
            } else {
                shouldWrite = true;
            }
            merge(root);
            if (shouldWrite) write();
            ConfigFileWatcher.registerWatch(this);
        }
    }

    private void merge(ConfigOption configGroup) {
        for (ConfigBase<?> entry : configGroup.getEntries()) {
            if (entry instanceof ConfigValue<?> configValue) {
                safeSet(configValue);
            } else if (entry instanceof ConfigOption group) {
                merge(group);
            }
        }
    }

    private static <T> void safeSet(ConfigValue<T> configValue) {
        if (configValue.get() == null) configValue.set(configValue.getDefault());
    }

    private void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            manager.error(e.getMessage());
        }
    }
}