package dev.proststuff.utilitary.config.utility;

import dev.proststuff.utilitary.Utilitary;
import dev.proststuff.utilitary.config.Config;
import dev.proststuff.utilitary.config.ConfigFile;
import net.minecraft.server.network.ServerPlayerEntity;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UtilitaryFileWatcher {
    private static final List<ConfigFile> watchedConfigFiles = new ArrayList<>();
    private static WatchService watchService;
    private static ExecutorService watcherExecutor;

    public static boolean isConfigFileWatched(ConfigFile configFile) {
        for (ConfigFile watchedConfigFile : watchedConfigFiles) {
            if (watchedConfigFile.getPath().equals(configFile.getPath())) return true;
        }

        return false;
    }

    public static void registerWatch(ConfigFile configFile) {
        try {
            if (watchService == null) {
                watchService = FileSystems.getDefault().newWatchService();
                watcherExecutor = Executors.newSingleThreadExecutor();
                startWatcherThread();
            }

            Path configFilePath = configFile.getPath();
            Path dir = configFilePath.getParent();

            if (!isConfigFileWatched(configFile)) {
                dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
                watchedConfigFiles.add(configFile);
            }
        } catch (Exception e) {
            Utilitary.LOGGER.error("Unable to register config watch. Got {}", String.valueOf(e));
        }
    }

    private static void startWatcherThread() {
        watcherExecutor.submit(() -> {
            Map<Path, Long> lastModifiedTimes = new HashMap<>();

            try {
                while (!Thread.currentThread().isInterrupted()) {
                    WatchKey key = watchService.take();

                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() != StandardWatchEventKinds.ENTRY_MODIFY) continue;

                        @SuppressWarnings("unchecked")
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path changedFile = ev.context();
                        Path changedAbsolute = ((Path) key.watchable()).resolve(changedFile).toAbsolutePath();

                        for (ConfigFile watchedConfigFile : watchedConfigFiles) {
                            Path configFilePath = watchedConfigFile.getPath();

                            if (configFilePath.toAbsolutePath().equals(changedAbsolute)) {
                                long currentTime = System.currentTimeMillis();

                                if (lastModifiedTimes.getOrDefault(changedAbsolute, 0L) + 100 < currentTime) {
                                    lastModifiedTimes.put(changedAbsolute, currentTime);

                                    watchedConfigFile.read();
                                }
                            }
                        }

                        if (Utilitary.getServer() != null) {
                            for (ServerPlayerEntity player : Utilitary.getServer().getPlayerManager().getPlayerList()) {
                                Config.syncToPlayer(player);
                            }
                        }
                    }

                    key.reset();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                Utilitary.LOGGER.error("Unable to start watch thread. Got {}", String.valueOf(e));
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

            if (Utilitary.CONFIG.debugEnabled()) Utilitary.LOGGER.info("Stopped watching all of {} config", configEnvironment);
        } catch (Exception e) {
            Utilitary.LOGGER.error("Unable to stop watching. Got {}", String.valueOf(e));
        }
    }
}
