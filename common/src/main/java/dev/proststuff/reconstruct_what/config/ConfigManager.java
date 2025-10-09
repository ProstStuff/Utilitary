package dev.proststuff.reconstruct_what.config;

import com.google.gson.JsonElement;
import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.config.instance.ConfigHolder;
import dev.proststuff.reconstruct_what.platform.services.IPlatformHelper;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigManager implements IFancyLogging {
    private static final IPlatformHelper PLATFORM = ReconstructWhat.getPlatform();

    private static final ExecutorService CONFIG_EXECUTOR = Executors.newSingleThreadExecutor(r ->
            new Thread(r, "RECONSTRUCT WHAT")
    );

    private static final Set<ConfigManager> configManagers = new LinkedHashSet<>();

    public final String MOD_ID;
    // TODO: Add custom folder name
    //public final String NAME;
    public final Logger LOGGER;
    public boolean DEBUG = false;

    private final boolean clientOnly = false;

    private final Map<String, ConfigHolder> configurations = new LinkedHashMap<>();
    private ConfigHolder common;
    private ConfigHolder client;
    private ConfigHolder server;

    public ConfigManager(String modId) {
        this.MOD_ID = modId;
        this.LOGGER = LoggerFactory.getLogger(ReconstructWhat.NAME + " - config/" + modId);
        configManagers.add(this);
    }

    public ConfigManager(String modId, boolean common, boolean client, boolean server) {
        this(modId);

        if (common) this.addCommon();
        if (client) this.addClient();
        if (server) this.addServer();
    }

    public static void loadAll(ConfigHelper.ConfigType type, @Nullable MinecraftServer server, boolean saveLater) {
        configManagers.forEach(manager -> manager.loadSpecific(type, server, saveLater));
    }

    public static void saveAll(ConfigHelper.ConfigType type, @Nullable MinecraftServer server) {
        configManagers.forEach(manager -> manager.saveSpecific(type, server));
    }

    public void loadSpecific(ConfigHelper.ConfigType type, @Nullable MinecraftServer server, boolean saveLater) {
        this.info(LogType.ACTION, "Loading {} config for {}", type.toString(), this.MOD_ID);

        configurations.forEach((name, holder) -> {
            if (holder.getType() == type) {
                Path configPath;
                if (type == ConfigHelper.ConfigType.SERVER && server != null) {
                    configPath = ConfigHelper.getServerConfigDirectoryFor(server, this.MOD_ID);
                } else {
                    configPath = ConfigHelper.getConfigDirectoryFor(this.MOD_ID);
                }

                CompletableFuture.runAsync(() -> {
                    this.info(LogType.SUB, "Preparing {} at {}", name, configPath);
                    this.load(holder, configPath);
                    holder.registerWatch(configPath, this);
                }, CONFIG_EXECUTOR).thenRun(() -> {
                            this.info(LogType.DONE, "Finished loading '{}'", holder.getName());

                            if (saveLater) {
                                this.save(holder, configPath);
                            }
                        }
                );
            }
        });
    }

    public void saveSpecific(ConfigHelper.ConfigType type, @Nullable MinecraftServer server) {
        this.info(LogType.ACTION, "Saving config for {}", type.toString());

        configurations.forEach((name, holder) -> {
            if (holder.getType() == type) {
                Path configPath;
                if (type == ConfigHelper.ConfigType.SERVER && server != null) {
                    configPath = ConfigHelper.getServerConfigDirectoryFor(server, this.MOD_ID);
                } else {
                    configPath = ConfigHelper.getConfigDirectoryFor(this.MOD_ID);
                }

                CompletableFuture.runAsync(() -> {
                    this.info(LogType.SUB, "writing {} to {}", name, configPath);
                    this.save(holder, configPath);
                }, CONFIG_EXECUTOR).thenRun(() ->
                        this.info(LogType.DONE,"Saved '{}'", holder.getName())
                );
            }
        });
    }

    private void load(ConfigHolder config, Path path) {
        this.info(LogType.DETAIL, "Loading '{}'", path);
        ConfigHelper.createDirectory(path);
        config.save(path.resolve(config.getName() + ".json"), this);
    }

    private void save(ConfigHolder config, Path path) {
        this.info(LogType.DETAIL, "Saving '{}'", path);
        ConfigHelper.createDirectory(path);
        config.load(path.resolve(config.getName() + ".json"), this);
    }

    public ConfigManager makeConfig(String name, ConfigHelper.ConfigType type) {
        if (configurations.get(name) != null) {
            throw new IllegalStateException("Duplicated configuration name. '" + name + "' already exist.");
        }

        ConfigHolder configHolder = new ConfigHolder(name, type);
        configurations.put(name, configHolder);

        switch (name) {
            case "common" -> this.common = configHolder;
            case "client" -> this.client = configHolder;
            case "server" -> this.server = configHolder;
        }

        return this;
    }

    public ConfigHolder getConfig(String name) {
        ConfigHolder config =  configurations.get(name);
        if (config == null) {
            throw new NoSuchElementException("Config with name " + name + " is not initiated.");
        }
        return config;
    }

    public Map<String, ConfigHolder> getConfigs() {
        return this.configurations;
    }

    public ConfigManager addCommon() {
        return this.makeConfig("common", ConfigHelper.ConfigType.COMMON);
    }

    public ConfigManager addClient() {
        return this.makeConfig("client", ConfigHelper.ConfigType.CLIENT);
    }

    public ConfigManager addServer() {
        return this.makeConfig("server", ConfigHelper.ConfigType.SERVER);
    }

    public ConfigHolder getCommon() {
        return common;
    }

    public ConfigHolder getClient() {
        return client;
    }

    public ConfigHolder getServer() {
        return server;
    }

    // Sync

    public void syncToPlayer(ServerPlayer player) {
        info(LogType.ACTION, "Syncing server & common config to {}", player.getName().getString());

        configurations.forEach((name, holder) -> {
            if (holder.getType() == ConfigHelper.ConfigType.SERVER || holder.getType() == ConfigHelper.ConfigType.COMMON) {
                JsonElement data = holder.serialize(this);
                PLATFORM.syncConfigToPlayer(player, MOD_ID, data);

                info(LogType.SUB, "Sent {} bytes of data to {} config", data.toString().getBytes(StandardCharsets.UTF_8).length, holder.getName());
            }
        });
    }

    public void applySyncedData(JsonElement json) {
        if (client == null) return;

        info(LogType.ACTION, "Applying synced server config to client");
        client.deserialize(json, this);
        info(LogType.DONE, "↳ Sync applied successfully");
    }

    // Others

    public static ExecutorService getConfigExecutor() {
        return CONFIG_EXECUTOR;
    }

    @Override
    public Logger getLogger() {
        return this.LOGGER;
    }

    public static ConfigManager getManager(String modId) {
        for (ConfigManager manager : configManagers) {
            if (manager.MOD_ID.equals(modId)) {
                return manager;
            }
        }
        return null;
    }

    public static Set<ConfigManager> getManagers() {
        return configManagers;
    }
}