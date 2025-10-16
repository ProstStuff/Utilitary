package dev.proststuff.reconstruct_what;

import dev.proststuff.reconstruct_what.config.ConfigHelper;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.instance.ConfigHolder;
import dev.proststuff.reconstruct_what.platform.RWAbstractPlatform;
import dev.proststuff.reconstruct_what.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReconstructWhat {
    public static final String ID = "reconstruct_what";
    public static final String NAME = "RECONSTRUCT WHAT";
    public static final Logger LOG = LoggerFactory.getLogger(NAME);
    private static final RWAbstractPlatform PLATFORM = Services.load(RWAbstractPlatform.class);

    public static void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ConfigHolder.stopWatching(ConfigHelper.ConfigType.COMMON);
            ConfigHolder.stopWatching(ConfigHelper.ConfigType.CLIENT);
            ConfigHolder.stopWatching(ConfigHelper.ConfigType.SERVER);
            ConfigManager.getConfigExecutor().shutdown();
        }));

        RWConfigExample.init();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    public static void loadConfigs(ConfigHelper.ConfigType configType, @Nullable MinecraftServer server) {
        ConfigManager.loadAll(configType, server, true);
    }

    public static void saveConfigs(ConfigHelper.ConfigType configType, @Nullable MinecraftServer server) {
        ConfigManager.saveAll(configType, server);
    }

    public static RWAbstractPlatform getPlatform() {
        return PLATFORM;
    }
}