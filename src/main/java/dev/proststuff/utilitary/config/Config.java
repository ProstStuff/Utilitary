package dev.proststuff.utilitary.config;

import com.google.gson.JsonElement;
import dev.proststuff.utilitary.config.network.ServerBoundConfigSyncPacket;
import dev.proststuff.utilitary.config.utility.ConfigCompressor;
import dev.proststuff.utilitary.config.utility.ConfigEnvironment;
import dev.proststuff.utilitary.config.value.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Config {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor(r -> new Thread(r, "Utilitary Config"));
    private static final Map<String, Config> CONFIGS = new LinkedHashMap<>();

    protected final String name;
    protected final Logger logger;
    protected final boolean debug;

    protected Map<Identifier, ConfigFile> configFiles = new HashMap<>();

    public Config(String name, String loggerName) {
        this.name = name;
        this.logger = LoggerFactory.getLogger(loggerName);
        this.debug = false;

        CONFIGS.put(name, this);
    }

    public Config(String name) {
        this(name, name + "/CONFIG");
    }

    public ConfigFile add(String name, ConfigEnvironment environment) {
        Identifier fileId = of(name);
        ConfigFile file = new ConfigFile(of(name), environment);
        configFiles.put(fileId, file);
        return file;
    }

    public void save(ConfigEnvironment configEnvironment) {
        configFiles.forEach((id, file) -> {
            if (configEnvironment == file.environment) {
                CompletableFuture.runAsync(file::write, EXECUTOR_SERVICE);
            }
        });
    }

    public void load(ConfigEnvironment configEnvironment, boolean saveLater) {
        CompletableFuture.runAsync(() -> configFiles.forEach((id, file) -> {
            if (configEnvironment == file.environment) {
                CompletableFuture.runAsync(file::read, EXECUTOR_SERVICE);
            }
        }), EXECUTOR_SERVICE).thenRun(() -> {
            if (saveLater) {
                save(configEnvironment);
            }
        });
    }

    public ConfigFile get(Identifier name) {
        return configFiles.get(name);
    }

    public ConfigFile get(String name) {
        return get(of(name));
    }

    public boolean debugEnabled() {
        return FabricLoader.getInstance().isDevelopmentEnvironment() || debug;
    }

    public Identifier of(String path) {
        return Identifier.of(name, path);
    }

    public Logger getLogger() {
        return logger;
    }

    public ConfigString s(String name, String value) {
        return new ConfigString(of(name), value);
    }

    public ConfigBoolean b(String name, boolean value) {
        return new ConfigBoolean(of(name), value);
    }

    public ConfigInteger i(String name, int value) {
        return new ConfigInteger(of(name), value);
    }

    public ConfigInteger i(String name, int value, int min, int max) {
        return new ConfigInteger(of(name), value, min, max);
    }

    public ConfigDouble d(String name, double value) {
        return new ConfigDouble(of(name), value);
    }

    public ConfigDouble d(String name, double value, double min, double max) {
        return new ConfigDouble(of(name), value, min, max);
    }

    public ConfigFloat f(String name, float value) {
        return new ConfigFloat(of(name), value);
    }

    public ConfigFloat f(String name, float value, float min, float max) {
        return new ConfigFloat(of(name), value, min, max);
    }

    public static void loadSided(ConfigEnvironment environment, boolean saveLater) {
        CONFIGS.forEach((name, config) -> config.load(environment, saveLater));
    }

    public static void saveSided(ConfigEnvironment environment) {
        CONFIGS.forEach((name, config) -> config.save(environment));
    }

    public static Config getConfig(String name) {
        return CONFIGS.get(name);
    }

    public static void syncToPlayer(ServerPlayerEntity serverPlayer) {
        if (!ServerPlayNetworking.canSend(serverPlayer, ServerBoundConfigSyncPacket.ID)) return;

        for (Config config : CONFIGS.values()) {
            for (ConfigFile file : config.configFiles.values()) {
                if (file.getEnvironment() == ConfigEnvironment.COMMON || file.getEnvironment() == ConfigEnvironment.SERVER) {
                    JsonElement jsonData = file.encode();

                    try {
                        byte[] compressed = ConfigCompressor.compress(jsonData.toString().getBytes(StandardCharsets.UTF_8));

                        final int MAX_CHUNK_SIZE = 512 * 1024;
                        int totalChunks = (int) Math.ceil((double) compressed.length / MAX_CHUNK_SIZE);

                        if (config.debugEnabled()) config.logger.info("Syncing {} config ({} bytes compressed, created {} chunks) to {}", config.name, compressed.length, totalChunks, serverPlayer.getName().getString());

                        for (int i = 0; i < totalChunks; i++) {
                            int start = i * MAX_CHUNK_SIZE;
                            int end = Math.min(compressed.length, start + MAX_CHUNK_SIZE);
                            byte[] chunk = Arrays.copyOfRange(compressed, start, end);

                            ServerBoundConfigSyncPacket packet = new ServerBoundConfigSyncPacket(
                                    config.name,
                                    file.identifier,
                                    i,
                                    totalChunks,
                                    Base64.getEncoder().encodeToString(chunk),
                                    true
                            );

                            ServerPlayNetworking.send(serverPlayer, packet);
                        }

                    } catch (IOException e) {
                        config.logger.error("Unable to compress and send {}.json to {}", file.identifier.getPath(), serverPlayer.getName());
                    }
                }
            }
        }
    }
}
