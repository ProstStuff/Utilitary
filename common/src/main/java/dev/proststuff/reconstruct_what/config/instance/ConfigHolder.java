package dev.proststuff.reconstruct_what.config.instance;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.config.ClientBoundConfigSyncPacket;
import dev.proststuff.reconstruct_what.config.ConfigHelper;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.ICanConfigure;
import dev.proststuff.reconstruct_what.platform.AbstractPlatform;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;
import net.minecraft.server.level.ServerPlayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("unchecked")
public class ConfigHolder implements ICanConfigure<ConfigGroup> {
    private static final AbstractPlatform PLATFORM = ReconstructWhat.getPlatform();
    private static final Object config_lock = new Object();
    private static final Map<Path, ConfigHolder> watchedConfigs = new HashMap<>();
    private static WatchService watchService;
    private static ExecutorService watcherExecutor;

    private final String name;
    private final ConfigGroup rootGroup;
    private final ConfigHelper.ConfigType type;
    private Path path;
    private final boolean syncToClient;

    private Consumer<ConfigManager> serializingEvent;
    private Consumer<ConfigManager> serializedEvent;
    private Consumer<ConfigManager> deserializingEvent;
    private Consumer<ConfigManager> deserializedEvent;

    private boolean initialized = false;

    public ConfigHolder(String name, ConfigHelper.ConfigType type) {
        this.name = name;
        this.type = type;
        this.syncToClient = type == ConfigHelper.ConfigType.COMMON || type == ConfigHelper.ConfigType.SERVER;
        this.rootGroup = new ConfigGroup(this.name);
    }

    public <T extends ICanConfigure<?>> ConfigHolder add(T entry) {
        rootGroup.add(entry);
        return this;
    }

    public boolean shouldSync() {return this.syncToClient;}

    public void save(Path path, ConfigManager manager) {
        this.initialized = true;
        this.path = path;

        try (Writer writer = Files.newBufferedWriter(path)) {

            JsonObject serialized = serialize(manager).getAsJsonObject();
            ConfigHelper.GSON.toJson(serialized, writer);
            manager.info(IFancyLogging.LogType.DONE, "Saved '{}' config for {} to {}", name, manager.NAME, path.toAbsolutePath());
        } catch (IOException e) {
            manager.error(IFancyLogging.LogType.ERROR, "Unable to save {} {} config: {}", manager.NAME, name, e);
        }

        rootGroup.serialized(manager);
    }

    public void load(Path path, ConfigManager manager) {
        this.initialized = true;
        this.path = path;

        synchronized (config_lock) {
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
            }

            mergeDefaults();

            if (shouldSave) {
                save(path, manager);
            }
        }

        rootGroup.deserialized(manager);
    }

    public boolean isInitialized() {return initialized;}

    private void mergeDefaults() {mergeGroupDefaults(rootGroup);}

    private void mergeGroupDefaults(ConfigGroup group) {
        group.getEntries().forEach((key, entry) -> {
            if (entry instanceof ConfigValue<?> configValue && !configValue.wasLoaded()) {
                safeSet(configValue);
            } else if (entry instanceof ConfigGroup subgroup) {
                mergeGroupDefaults(subgroup);
            }
        });
    }

    private static <T> void safeSet(ConfigValue<T> configValue) {if (configValue.get() == null) configValue.set(configValue.getDefault());}

    @Override
    public JsonElement serialize(ConfigManager manager) {
        rootGroup.serializing(manager);
        return rootGroup.serialize(manager);
    }

    @Override
    public void deserialize(JsonElement element, ConfigManager manager) {
        rootGroup.deserializing(manager);
        if (element.isJsonObject()) {
            rootGroup.deserialize(element.getAsJsonObject(), manager);
        }
    }

    @Override
    public String getName() {return this.name;}
    @Override
    public ConfigGroup get() {return this.rootGroup;}
    public void serializing(Consumer<ConfigManager> event) {this.serializingEvent = event;}
    public void serialized(Consumer<ConfigManager> event) {this.serializedEvent = event;}
    public void deserializing(Consumer<ConfigManager> event) {this.deserializingEvent = event;}
    public void deserialized(Consumer<ConfigManager> event) {this.deserializedEvent = event;}
    public ICanConfigure<?> get(String name) {return this.rootGroup.get(name);}
    public ConfigHelper.ConfigType getType() {return this.type;}

    @Override
    public void serializing(ConfigManager configManager) {
        if (this.serializingEvent != null) this.serializingEvent.accept(configManager);
        rootGroup.serializing(configManager);
    }

    @Override
    public void serialized(ConfigManager configManager) {if (this.serializedEvent != null) this.serializedEvent.accept(configManager);
        rootGroup.serialized(configManager);
    }

    @Override
    public void deserializing(ConfigManager configManager) {
        if (this.deserializingEvent != null) this.deserializingEvent.accept(configManager);
        rootGroup.deserializing(configManager);
    }

    @Override
    public void deserialized(ConfigManager configManager) {
        if (this.deserializedEvent != null) this.deserializedEvent.accept(configManager);
        rootGroup.deserialized(configManager);
    }

    public void syncToClient(ServerPlayer player, ConfigManager manager) {
        if (!this.shouldSync()) return;

        JsonElement data = serialize(manager);
        manager.info(IFancyLogging.LogType.ACTION, "Syncing '{}' config to client...", data);

        syncConfigToPlayer(PLATFORM, player, manager.NAME, this.name, data);
        manager.info(IFancyLogging.LogType.SUB, "Sent {} bytes of data to {} config", data.toString().getBytes(StandardCharsets.UTF_8).length, this.getName());
    }

    public void applySynced(JsonObject json, ConfigManager manager) {
        manager.info(IFancyLogging.LogType.ACTION, "Received synced '{}' config", name);
        deserialize(json, manager);
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

    public static void syncConfigToPlayer(AbstractPlatform platform, ServerPlayer player, String modId, String configName, JsonElement jsonData) {
        try {
            byte[] compressed = compress(jsonData.toString().getBytes(StandardCharsets.UTF_8));

            final int MAX_CHUNK_SIZE = 512 * 1024;
            int totalChunks = (int) Math.ceil((double) compressed.length / MAX_CHUNK_SIZE);

            ReconstructWhat.LOG.info("Syncing {} config ({} bytes compressed, created {} chunks) to {}",
                    modId, compressed.length, totalChunks, player.getName().getString());

            for (int i = 0; i < totalChunks; i++) {
                int start = i * MAX_CHUNK_SIZE;
                int end = Math.min(compressed.length, start + MAX_CHUNK_SIZE);
                byte[] chunk = Arrays.copyOfRange(compressed, start, end);

                ClientBoundConfigSyncPacket packet = new ClientBoundConfigSyncPacket(
                        modId,
                        configName,
                        i,
                        totalChunks,
                        Base64.getEncoder().encodeToString(chunk),
                        true
                );
                platform.sendToPlayer(player, packet);
            }

        } catch (IOException e) {
            ReconstructWhat.LOG.error("Failed to compress config for '{}': {}", modId, e);
        }
    }

    private static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
            gzip.write(data);
        }
        return bos.toByteArray();
    }
}