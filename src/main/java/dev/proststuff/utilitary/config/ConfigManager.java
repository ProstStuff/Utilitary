package dev.proststuff.utilitary.config;

import com.google.gson.JsonElement;
import dev.proststuff.utilitary.Utilitary;
import dev.proststuff.utilitary.utility.IFancyLogging;
import dev.proststuff.utilitary.utility.config.ConfigEnvironment;
import dev.proststuff.utilitary.utility.config.ConfigPress;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigManager implements IFancyLogging {
    private static final ExecutorService configExecutor = Executors.newSingleThreadExecutor(r -> new Thread(r, "Reconstruct What Config Thread"));
    private static final List<ConfigManager> managers = new ArrayList<>();

    public final String NAME;
    public final List<ConfigFile> configFiles = new ArrayList<>();
    public final Logger LOGGER;
    
    private boolean debug = false;

    public ConfigManager(String name, String loggerName) {
        for (ConfigManager manager : managers) {
            if (manager.NAME.equals(name)) throw new IllegalStateException("Another mod is using the same Reconstruct What's ConfigManager.");
        }

        this.NAME = name;
        this.LOGGER = LoggerFactory.getLogger(loggerName);

        managers.add(this);
    }

    public ConfigManager(String name) {
        this(name, name + " (Config)");
    }
    
    public ConfigManager setDebugEnable(boolean enable) {
        this.debug = enable;
        return this;
    }

    public ConfigFile getConfigFile(String name) {
        for (ConfigFile configFile : configFiles) {
            if (configFile.getName().equals(name)) return configFile;
        }

        return null;
    }

    public ConfigFile newFile(String name, ConfigEnvironment environment) {
        if (getConfigFile(name) != null) throw new IllegalStateException("Trying to create a new ConfigFile with an existing name!");
        ConfigFile file = new ConfigFile(name, environment);
        file.setConfigManager(this);

        configFiles.add(file);
        return file;
    }

    public void save(ConfigEnvironment configEnvironment) {
        info("Saving {} configs", NAME);
        for (ConfigFile configFile : configFiles) {
            if (configEnvironment != null) {if (!(configFile.environment == configEnvironment)) continue;}
            CompletableFuture.runAsync(configFile::write, configExecutor);
        }
    }

    public void load(ConfigEnvironment configEnvironment, boolean saveLater) {
        info("Loading config", NAME);
        CompletableFuture.runAsync(() -> {
                    for (ConfigFile configFile : configFiles) {
                        if (configEnvironment != null) if (!(configFile.environment == configEnvironment)) continue;

                        configFile.read();
                    }
            }, configExecutor)
                .thenRun(() -> {
                    info(LogType.SUB, "Finished loading config");
                    if (saveLater) {
                        info(LogType.SUB, "Saving config to patch some values");
                        save(configEnvironment);
                    } else {
                        info(LogType.DONE, "No saving is requested by the method");
                    }
                });
    }

    public static void saveFor(ConfigEnvironment environment) {
        for (ConfigManager manager : managers) {
            manager.save(environment);
        }
    }

    public static void loadFor(ConfigEnvironment environment) {
        for (ConfigManager manager : managers) {
            manager.load(environment, true);
        }
    }

    public static ConfigManager getManager(String name) {
        for (ConfigManager manager : managers) {
            if (manager.NAME.equals(name)) {
                return manager;
            }
        }

        return null;
    }

    public static ConfigManager getManagerOrThrow(String name) {
        ConfigManager manager = getManager(name);

        if (manager != null) {
            return manager;
        } else {
            throw new IllegalStateException("Unable to find a config manager with name " + name);
        }
    }

    public static void sync() {
        if (Utilitary.SERVER != null) {
            for (ServerPlayerEntity player : Utilitary.SERVER.getPlayerManager().getPlayerList()) {
                syncToPlayer(player);
            }
        }
    }

    public static void syncToPlayer(ServerPlayerEntity serverPlayer) {
        if (!ServerPlayNetworking.canSend(serverPlayer, ServerBoundConfigSyncPacket.ID)) return;

        for (ConfigManager manager : managers) {
            for (ConfigFile configFile : manager.configFiles) {
                if (configFile.getEnvironment() == ConfigEnvironment.COMMON || configFile.getEnvironment() == ConfigEnvironment.SERVER) {
                    JsonElement jsonData = configFile.encode();

                    try {
                        byte[] compressed = ConfigPress.compress(jsonData.toString().getBytes(StandardCharsets.UTF_8));

                        final int MAX_CHUNK_SIZE = 512 * 1024;
                        int totalChunks = (int) Math.ceil((double) compressed.length / MAX_CHUNK_SIZE);

                        manager.info("Syncing {} config ({} bytes compressed, created {} chunks) to {}",
                                manager.NAME, compressed.length, totalChunks, serverPlayer.getName().getString());

                        for (int i = 0; i < totalChunks; i++) {
                            int start = i * MAX_CHUNK_SIZE;
                            int end = Math.min(compressed.length, start + MAX_CHUNK_SIZE);
                            byte[] chunk = Arrays.copyOfRange(compressed, start, end);

                            ServerBoundConfigSyncPacket packet = new ServerBoundConfigSyncPacket(
                                    manager.NAME,
                                    configFile.name,
                                    i,
                                    totalChunks,
                                    Base64.getEncoder().encodeToString(chunk),
                                    true
                            );

                            ServerPlayNetworking.send(serverPlayer, packet);
                        }

                    } catch (IOException e) {
                        manager.errorWithStackTrace(e, "Unable to compress and send {}.json to {}", configFile.name, serverPlayer.getName());
                    }
                }
            }
        }
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public boolean canPrint() {
        return debug;
    }
}