package dev.proststuff.reconstruct_what.config.instance;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.config.ConfigHelper;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.ICanConfigure;
import dev.proststuff.reconstruct_what.platform.services.IPlatformHelper;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;
import net.minecraft.server.level.ServerPlayer;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unchecked")
public class ConfigHolder implements ICanConfigure<ConfigGroup> {
    private static IPlatformHelper PLATFORM = ReconstructWhat.getPlatform();
    private static final Object CONFIG_LOCK = new Object();

    private final String name;
    private final ConfigGroup rootGroup;
    private final ConfigHelper.ConfigType type;
    private Path path;
    private final boolean syncToClient;

    private static final Map<Path, ConfigHolder> watchedConfigs = new HashMap<>();
    private static WatchService watchService;
    private static ExecutorService watcherExecutor;

    public ConfigHolder(String name, ConfigHelper.ConfigType type) {
        this.name = name;
        this.type = type;
        this.syncToClient = type == ConfigHelper.ConfigType.COMMON || type == ConfigHelper.ConfigType.SERVER;
        this.rootGroup = new ConfigGroup(this.name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ConfigGroup get() {
        return this.rootGroup;
    }

    public ConfigHelper.ConfigType getType() {
        return this.type;
    }

    public <T extends ICanConfigure<?>> ConfigHolder add(T entry) {
        rootGroup.add(entry);
        return this;
    }

    public boolean shouldSync() {
        return this.syncToClient;
    }

    public void save(Path path, ConfigManager manager) {
        this.path = path;
        try (Writer writer = Files.newBufferedWriter(path)) {
            rootGroup.preSave(manager);

            JsonObject serialized = serialize(manager).getAsJsonObject();
            ConfigHelper.GSON.toJson(serialized, writer);

            rootGroup.postSave(manager);
            manager.info(IFancyLogging.LogType.DONE, "Saved '{}' config for {} to {}", name, manager.MOD_ID, path.toAbsolutePath());
        } catch (IOException e) {
            manager.error(IFancyLogging.LogType.ERROR, "Unable to save {} {} config: {}", manager.MOD_ID, name, e);
        }
    }

    public void load(Path path, ConfigManager manager) {
        synchronized (CONFIG_LOCK) {
            this.path = path;
            if (!Files.exists(path)) {
                manager.info(IFancyLogging.LogType.SUB, "'{}' Config file not found, generating defaults for {}", name, manager.MOD_ID);
                save(path, manager);
                return;
            }

            try (Reader reader = Files.newBufferedReader(path)) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                deserialize(json, manager);
                mergeDefaults();
                save(path, manager);
            } catch (IOException e) {
                manager.error(IFancyLogging.LogType.ERROR, "Unable to load {} {} config: {}", manager.MOD_ID, name, e);
                save(path, manager);
            }
        }
    }

    private void mergeDefaults() {
        mergeGroupDefaults(rootGroup);
    }

    private void mergeGroupDefaults(ConfigGroup group) {
        group.getEntries().forEach((key, entry) -> {
            if (entry instanceof AbstractConfigValue<?> configValue && configValue.get() == null) {
                safeSet(configValue);
            } else if (entry instanceof ConfigGroup subgroup) {
                mergeGroupDefaults(subgroup);
            }
        });
    }

    private static <T> void safeSet(AbstractConfigValue<T> configValue) {
        if (configValue.get() == null) configValue.set(configValue.getDefault());
    }

    @Override
    public JsonElement serialize(ConfigManager manager) {
        return rootGroup.serialize(manager);
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        if (element.isJsonObject()) {
            rootGroup.deserialize(element.getAsJsonObject(), manager);
        }
    }

    public void syncToClient(ServerPlayer player, ConfigManager manager) {
        if (!this.shouldSync()) return;

        JsonElement json = serialize(manager);
        manager.info(IFancyLogging.LogType.ACTION, "Syncing '{}' config to client...", name);

        PLATFORM.syncConfigToPlayer(player, manager.MOD_ID, json);
    }

    public void applySynced(JsonObject json, ConfigManager manager) {
        manager.info(IFancyLogging.LogType.ACTION, "Received synced '{}' config", name);
        deserialize(json, manager);
        onChange();
    }

    //TODO: Fix multiple registerWatch() call to the same ConfigHolder
    public void registerWatch(Path configPath, ConfigManager configManager) {
        try {
            if (watchService == null) {
                watchService = FileSystems.getDefault().newWatchService();
                watcherExecutor = Executors.newSingleThreadExecutor();
                startWatcherThread(configManager);
            }

            Path dir = configPath.getParent();

            if (!watchedConfigs.containsKey(configPath)) {
                dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
                watchedConfigs.put(configPath, this);
                configManager.info(IFancyLogging.LogType.DETAIL, "Watching {} config for changes", configPath);
            } else {
                configManager.info(IFancyLogging.LogType.WARN, "Already watching {}", configPath);
            }
        } catch (Exception e) {
            configManager.error(IFancyLogging.LogType.ERROR, "Error while trying to register watch changes: ", e);
        }
    }

    private static void startWatcherThread(ConfigManager configManager) {
        watcherExecutor.submit(() -> {
            Map<Path, Long> lastModifiedTimes = new HashMap<>();

            try {
                while (!Thread.currentThread().isInterrupted()) {
                    WatchKey key = watchService.take();

                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() != StandardWatchEventKinds.ENTRY_MODIFY) continue;

                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path changedFile = ev.context();
                        Path changedAbsolute = ((Path) key.watchable()).resolve(changedFile).toAbsolutePath();

                        watchedConfigs.forEach((filePath, configHolder) -> {
                            if (filePath.toAbsolutePath().equals(changedAbsolute)) {
                                long currentTime = System.currentTimeMillis();

                                if (lastModifiedTimes.getOrDefault(changedAbsolute, 0L) + 100 < currentTime) {
                                    lastModifiedTimes.put(changedAbsolute, currentTime);

                                    configManager.info(IFancyLogging.LogType.ACTION,
                                            "Detected {} config changes", changedAbsolute);
                                    configHolder.load(configHolder.path, configManager);
                                    configHolder.onChange();
                                }
                            }
                        });
                    }
                    key.reset();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                configManager.error(IFancyLogging.LogType.ERROR, "Error while watching configs: {}", e);
            }
        });
    }

    public static void stopWatching(ConfigHelper.ConfigType type) {
        try {
            if (watchService == null) return;

            watchedConfigs.entrySet().removeIf(entry -> {
                ConfigHolder holder = entry.getValue();
                return holder.getType() == type;
            });

            if (watchedConfigs.isEmpty()) {
                watchService.close();
                watcherExecutor.shutdownNow();
                watchService = null;
                watcherExecutor = null;
            }

            ReconstructWhat.LOG.info("Stopped watching configs of type {}", type);
        } catch (Exception e) {
            ReconstructWhat.LOG.error("Error while trying to stop watch changes: ", e);
        }
    }
}