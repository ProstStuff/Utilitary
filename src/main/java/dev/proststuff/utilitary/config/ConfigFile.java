package dev.proststuff.utilitary.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.proststuff.utilitary.Utilitary;
import dev.proststuff.utilitary.config.utility.ConfigEnvironment;
import dev.proststuff.utilitary.config.utility.UtilitaryFileWatcher;
import dev.proststuff.utilitary.config.utility.gson.ConfigValueAdapter;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public class ConfigFile extends ConfigBase {
    private static final Gson GSON = ConfigValueAdapter.GSON;
    private static final Object LOCK = new Object();

    protected final ConfigGroup ROOT;
    protected final ConfigEnvironment environment;

    public ConfigFile(Identifier name, ConfigEnvironment environment) {
        super(name);
        this.ROOT = new ConfigGroup(name);
        this.environment = environment;
    }

    public ConfigEnvironment getEnvironment() {
        return environment;
    }

    public Path getPath() {
        return Path.of("config", identifier.getNamespace(), identifier.getPath() + ".json");
    }

    public ConfigGroup get() {
        return ROOT;
    }

    public ConfigFile add(ConfigBase configBase) {
        ROOT.add(configBase);
        return this;
    }

    @Override
    public JsonElement encode() {
        return ROOT.encode();
    }

    @Override
    public void decode(JsonElement element) {
        ROOT.decode(element);
    }

    public void write() {
        synchronized (LOCK) {
            Path target = getPath();
            createDirectory(target.getParent());

            Path tempFile = target.resolveSibling(target.getFileName() + ".tmp");

            try (Writer writer = Files.newBufferedWriter(
                    tempFile,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            )) {
                JsonObject encoded = encode().getAsJsonObject();
                GSON.toJson(encoded, writer);
            } catch (IOException e) {
                Utilitary.LOGGER.error("Unable to write config. Got {}", String.valueOf(e));
                return;
            }

            try {
                Files.move(
                        tempFile,
                        target,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (IOException e) {
                Utilitary.LOGGER.error("Unable to replace config. Got {}", String.valueOf(e));
            }
        }
    }

    public void read() {
        synchronized (LOCK) {
            Path configFilePath = getPath();
            boolean shouldWrite = false;

            if (Files.exists(configFilePath)) {
                try {
                    String raw = Files.readString(configFilePath, StandardCharsets.UTF_8);
                    if (raw.isBlank()) {
                        shouldWrite = true;
                    } else {
                        JsonElement json = JsonParser.parseString(raw);

                        if (!json.isJsonObject()) {
                            shouldWrite = true;
                        } else {
                            decode(json.getAsJsonObject());
                        }
                    }

                } catch (Exception e) {
                    Utilitary.LOGGER.error("Unable to read config. Got {}", String.valueOf(e));
                    shouldWrite = true;
                }
            } else {
                shouldWrite = true;
            }

            if (shouldWrite) {
                write();
            }

            UtilitaryFileWatcher.registerWatch(this);
        }
    }

    private void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            Utilitary.LOGGER.error("Unable to register config watch. Got {}", String.valueOf(e));
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (ConfigBase child : ROOT.children) {
            str.append("[").append(child.toString()).append("]");
        }

        return "ConfigFile$"+ identifier.getPath() +".json${" + str + "}" ;
    }
}
