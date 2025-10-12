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
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class ConfigHolder implements ICanConfigure<ConfigGroup> {
    private static final IPlatformHelper PLATFORM = ReconstructWhat.getPlatform();
    private static final Object CONFIG_LOCK = new Object();

    private final String name;
    private final ConfigGroup rootGroup;
    private final ConfigHelper.ConfigType type;
    private Path path;
    private final boolean syncToClient;

    private static final Map<Path, ConfigHolder> watchedConfigs = new HashMap<>();
    private static WatchService watchService;
    private static ExecutorService watcherExecutor;

    private Consumer<ConfigManager> loaded;
    private Consumer<ConfigHelper.ConfigType> changed;
    private Consumer<ConfigManager> preSave;
    private Consumer<ConfigManager> postSave;

    private boolean initialized = false;

    public ConfigHolder(String name, ConfigHelper.ConfigType type) {
        this.name = name;
        this.type = type;
        this.syncToClient = type == ConfigHelper.ConfigType.COMMON || type == ConfigHelper.ConfigType.SERVER;
        this.rootGroup = new ConfigGroup(this.name);
    }

    public void onLoaded(Consumer<ConfigManager> loaded) {
        this.loaded = loaded;
    }

    public void onChanged(Consumer<ConfigHelper.ConfigType> changed) {
        this.changed = changed;
    }

    public void onPreSave(Consumer<ConfigManager> preSave) {
        this.preSave = preSave;
    }

    public void onPostSave(Consumer<ConfigManager> postSave) {
        this.postSave = postSave;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ConfigGroup get() {
        return this.rootGroup;
    }

    public ICanConfigure<?> get(String name) {
        return this.rootGroup.get(name);
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
        this.initialized = true;

        this.path = path;
        preSave(manager);
        try (Writer writer = Files.newBufferedWriter(path)) {
            rootGroup.preSave(manager);

            JsonObject serialized = serialize(manager).getAsJsonObject();
            ConfigHelper.GSON.toJson(serialized, writer);

            rootGroup.postSave(manager);
            manager.info(IFancyLogging.LogType.DONE, "Saved '{}' config for {} to {}", name, manager.NAME, path.toAbsolutePath());
        } catch (IOException e) {
            manager.error(IFancyLogging.LogType.ERROR, "Unable to save {} {} config: {}", manager.NAME, name, e);
        }
        postSave(manager);
    }

    public void load(Path path, ConfigManager manager) {
        this.initialized = true;

        synchronized (CONFIG_LOCK) {
            this.path = path;

            boolean exists = Files.exists(path);
            boolean shouldSave = false;

            if (!exists) {
                manager.info(IFancyLogging.LogType.SUB, "'{}' config file not found, generating defaults for {}", name, manager.NAME);
                shouldSave = true;
            } else {
                try (Reader reader = Files.newBufferedReader(path)) {
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    deserialize(json, manager);
                } catch (IOException e) {
                    manager.error(IFancyLogging.LogType.ERROR, "Unable to load {} {} config: {}", manager.NAME, name, e);
                    shouldSave = true;
                }

                loaded(manager);
            }

            mergeDefaults();

            if (shouldSave) {
                save(path, manager);
            }
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    private void mergeDefaults() {
        mergeGroupDefaults(rootGroup);
    }

    private void mergeGroupDefaults(ConfigGroup group) {
        group.getEntries().forEach((key, entry) -> {
            if (entry instanceof ConfigValue<?> configValue && !configValue.wasLoaded()) {
                safeSet(configValue);
            } else if (entry instanceof ConfigGroup subgroup) {
                mergeGroupDefaults(subgroup);
            }
        });
    }

    private static <T> void safeSet(ConfigValue<T> configValue) {
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

    @Override
    public void loaded(ConfigManager configManager) {
        if (this.loaded != null) {
            this.loaded.accept(configManager);
        }
        changed();
    }

    @Override
    public void changed() {
        if (this.changed != null) {
            this.changed.accept(type);
        }
    }

    @Override
    public void preSave(ConfigManager configManager) {
        if (this.preSave != null) {
            this.preSave.accept(configManager);
        }
    }

    @Override
    public void postSave(ConfigManager configManager) {
        if (this.postSave != null) {
            this.postSave.accept(configManager);
        }
        changed();
    }

    public void syncToClient(ServerPlayer player, ConfigManager manager) {
        if (!this.shouldSync()) return;

        JsonElement data = serialize(manager);
        manager.info(IFancyLogging.LogType.ACTION, "Syncing '{}' config to client...", data);

        PLATFORM.syncConfigToPlayer(player, manager.NAME, this.name, data);
        manager.info(IFancyLogging.LogType.SUB, "Sent {} bytes of data to {} config", data.toString().getBytes(StandardCharsets.UTF_8).length, this.getName());
    }

    public void applySynced(JsonObject json, ConfigManager manager) {
        manager.info(IFancyLogging.LogType.ACTION, "Received synced '{}' config", name);
        deserialize(json, manager);
        changed();
    }

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
                                            "Detected {} config changes", configHolder.getName());
                                    configHolder.load(configHolder.path, configManager);
                                    configHolder.changed();
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