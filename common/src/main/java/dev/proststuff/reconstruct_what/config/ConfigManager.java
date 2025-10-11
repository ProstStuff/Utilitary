package dev.proststuff.reconstruct_what.config;

import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.config.instance.ConfigHolder;
import dev.proststuff.reconstruct_what.platform.services.IPlatformHelper;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigManager implements IFancyLogging {
    private static final IPlatformHelper PLATFORM = ReconstructWhat.getPlatform();

    private static final ExecutorService CONFIG_EXECUTOR = Executors.newSingleThreadExecutor(r ->
            new Thread(r, "RECONSTRUCT WHAT!?")
    );

    private static final Set<ConfigManager> configManagers = new HashSet<>();

    public final String NAME;
    public final Logger LOGGER;
    public boolean DEBUG = false;

    private final Map<String, ConfigHolder> configurations = new LinkedHashMap<>();
    private ConfigHolder startup;
    private ConfigHolder common;
    private ConfigHolder client;
    private ConfigHolder server;

    public ConfigManager(String name) {
        this.NAME = name;
        this.LOGGER = LoggerFactory.getLogger(ReconstructWhat.NAME + " - config/" + name);
        configManagers.add(this);
    }

    public ConfigManager(String name, boolean startup, boolean common, boolean client, boolean server) {
        this(name);

        if (startup) this.makeConfig("startup", ConfigHelper.ConfigType.STARTUP);
        if (common) this.makeConfig("common", ConfigHelper.ConfigType.COMMON);
        if (client) this.makeConfig("client", ConfigHelper.ConfigType.CLIENT);
        if (server) this.makeConfig("server", ConfigHelper.ConfigType.SERVER);
    }

    public static void loadAll(ConfigHelper.ConfigType type, @Nullable MinecraftServer server, boolean saveLater) {
        configManagers.forEach(manager -> manager.loadSpecific(type, server, saveLater));
    }

    public static void saveAll(ConfigHelper.ConfigType type, @Nullable MinecraftServer server) {
        configManagers.forEach(manager -> manager.saveSpecific(type, server));
    }

    public void loadSpecific(ConfigHelper.ConfigType type, @Nullable MinecraftServer server, boolean saveLater) {
        this.info(LogType.ACTION, "Loading {} config for {}", type, this.NAME);

        configurations.forEach((name, holder) -> {
            if (holder.getType() != type) return;

            Path configDir = (type == ConfigHelper.ConfigType.SERVER && server != null)
                    ? getServerConfigPath(server)
                    : getConfigPath();

            Path configFile = configDir.resolve(name + ".json").toAbsolutePath();

            CompletableFuture.runAsync(() -> {
                this.info(LogType.SUB, "Preparing {} at {}", name, configFile);

                this.load(holder, configDir);
                //holder.registerWatch(configDir, this);
            }, CONFIG_EXECUTOR).thenRun(() -> {
                this.info(LogType.DONE, "Finished loading '{}'", holder.getName());

                if (saveLater) {
                    this.save(holder, configDir);
                }
            });
        });
    }

    public void saveSpecific(ConfigHelper.ConfigType type, @Nullable MinecraftServer server) {
        this.info(LogType.ACTION, "Saving {} config for {}", type, this.NAME);

        configurations.forEach((name, holder) -> {
            if (holder.getType() != type) return;

            Path configDir = (type == ConfigHelper.ConfigType.SERVER && server != null)
                    ? getServerConfigPath(server)
                    : getConfigPath();

            Path configFile = configDir.resolve(name + ".json").toAbsolutePath();

            CompletableFuture.runAsync(() -> {
                this.info(LogType.SUB, "Writing {} to {}", name, configFile);

                ConfigHelper.createDirectory(configDir);
                holder.save(configFile, this);

            }, CONFIG_EXECUTOR).thenRun(() -> {
                holder.registerWatch(getConfigPath().resolve(name + ".json"), this);
                this.info(LogType.DONE, "Saved '{}'", holder.getName());
            });
        });
    }

    private void load(ConfigHolder config, Path path) {
        this.info(LogType.DETAIL, "Loading '{}'", path);
        ConfigHelper.createDirectory(path);
        config.load(path.resolve(config.getName() + ".json"), this);
    }

    private void save(ConfigHolder config, Path path) {
        this.info(LogType.DETAIL, "Saving '{}'", path);
        ConfigHelper.createDirectory(path);
        config.save(path.resolve(config.getName() + ".json"), this);
    }

    public ConfigManager makeConfig(String name, ConfigHelper.ConfigType type) {
        if (configurations.get(name) != null) {
            throw new IllegalStateException("Duplicated configuration name. '" + name + "' already exist.");
        }
        info(LogType.ACTION, "Creating a new {} config named '{}'", type.toString(), name);
        ConfigHolder configHolder = new ConfigHolder(name, type);
        configurations.put(name, configHolder);

        switch (name) {
            case "startup" -> this.startup = configHolder;
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

    public ConfigHolder getStartup() {
        return startup;
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

    public Path getConfigPath() {
        return ConfigHelper.getConfigDirectoryFor(this.NAME);
    }

    public Path getServerConfigPath(MinecraftServer server) {
        return ConfigHelper.getServerConfigDirectoryFor(server, this.NAME);
    }

    // Sync

    public void syncToPlayer(ServerPlayer player) {
        info(LogType.ACTION, "Syncing server & common config to {}", player.getName().getString());

        configurations.forEach((name, holder) -> {
            if (holder.getType() == ConfigHelper.ConfigType.SERVER || holder.getType() == ConfigHelper.ConfigType.COMMON) {
                holder.syncToClient(player, this);
            }
        });
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
            if (manager.NAME.equals(modId)) {
                return manager;
            }
        }
        return null;
    }

    public static Set<ConfigManager> getManagers() {
        return configManagers;
    }
}