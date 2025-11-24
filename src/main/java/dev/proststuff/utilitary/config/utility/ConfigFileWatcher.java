package dev.proststuff.utilitary.config.utility;

import dev.proststuff.utilitary.Utilitary;
import dev.proststuff.utilitary.config.ConfigFile;
import dev.proststuff.utilitary.config.ConfigManager;
import dev.proststuff.utilitary.utility.FancyLogging;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Detects and load external changes when a ConfigFile is modified.
 * ConfigFile is finishRegistration when <code>ConfigFile.read()</code> is called.
 * @see ConfigFile
 */
@SuppressWarnings("unchecked")
public class ConfigFileWatcher {
    private static final List<ConfigFile> watchedConfigFiles = new ArrayList<>();
    private static WatchService watchService;
    private static ExecutorService watcherExecutor;

    public static boolean isConfigFileWatched(ConfigFile configFile) {
        for (ConfigFile watchedConfigFile : watchedConfigFiles) {
            if (watchedConfigFile.getFilePath().equals(configFile.getFilePath())) return true;
        }

        return false;
    }

    public static void registerWatch(ConfigFile configFile) {
        configFile.getConfigManager().info("Preparing to watch {}.json from {}", configFile.getName(), configFile.getConfigManager().NAME);

        try {
            if (watchService == null) {
                configFile.getConfigManager().info(FancyLogging.LogType.SUB, "Starting watcher thread, requested by {}", configFile.getConfigManager().NAME);
                watchService = FileSystems.getDefault().newWatchService();
                watcherExecutor = Executors.newSingleThreadExecutor();
                startWatcherThread();
            }

            Path configFilePath = configFile.getFilePath();
            Path dir = configFilePath.getParent();

            if (!isConfigFileWatched(configFile)) {
                configFile.getConfigManager().info(FancyLogging.LogType.SUB, "Watching {}.json", configFile.getName(), configFile.getConfigManager().NAME);
                dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
                watchedConfigFiles.add(configFile);
                configFile.getConfigManager().info(FancyLogging.LogType.SUB, "Watched {}.json", configFile.getName(), configFile.getConfigManager().NAME);
            }
        } catch (Exception e) {
            configFile.getConfigManager().errorWithStackTrace(e);
        }
    }

    private static void startWatcherThread() {
        AtomicReference<ConfigManager> configManager = new AtomicReference<>();

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

                        for (ConfigFile watchedConfigFile : watchedConfigFiles) {
                            configManager.set(watchedConfigFile.getConfigManager());
                            Path configFilePath = watchedConfigFile.getFilePath();

                            if (configFilePath.toAbsolutePath().equals(changedAbsolute)) {
                                long currentTime = System.currentTimeMillis();

                                if (lastModifiedTimes.getOrDefault(changedAbsolute, 0L) + 100 < currentTime) {
                                    lastModifiedTimes.put(changedAbsolute, currentTime);

                                    watchedConfigFile.read();
                                }
                            }
                        }

                        ConfigManager.sync();
                    }

                    key.reset();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                if (configManager.get() != null) {
                    configManager.get().errorWithStackTrace(e);
                }
            }
        });
    }

    public static void stopWatching(ConfigEnvironment configEnvironment) {
        try {
            if (watchService == null) return;

            watchedConfigFiles.removeIf(configFile -> configFile.getEnvironment() == configEnvironment);

            if (watchedConfigFiles.isEmpty()) {
                watchService.close();
                watcherExecutor.shutdownNow();
                watchService = null;
                watcherExecutor = null;
            }

            Utilitary.CONFIG.info("Stopped watching all of {} config", configEnvironment);
        } catch (Exception e) {
            Utilitary.CONFIG.errorWithStackTrace(e, "Can't stop watching {} config", configEnvironment);
        }
    }
}