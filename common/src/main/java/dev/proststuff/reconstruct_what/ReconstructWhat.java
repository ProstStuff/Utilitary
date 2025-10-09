package dev.proststuff.reconstruct_what;

import dev.proststuff.reconstruct_what.config.ConfigHelper;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.instance.ConfigHolder;
import dev.proststuff.reconstruct_what.config.instance.value.ConfigBool;
import dev.proststuff.reconstruct_what.config.instance.value.ConfigResourceLocation;
import dev.proststuff.reconstruct_what.platform.Services;
import dev.proststuff.reconstruct_what.platform.services.IPlatformHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ReconstructWhat {
    public static final String ID = "reconstruct_what";
    public static final String NAME = "RECONSTRUCT WHAT";
    public static final Logger LOG = LoggerFactory.getLogger(NAME);

    public static void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ConfigHolder.stopWatching(ConfigHelper.ConfigType.COMMON);
            ConfigHolder.stopWatching(ConfigHelper.ConfigType.CLIENT);
            ConfigHolder.stopWatching(ConfigHelper.ConfigType.SERVER);
            ConfigManager.getConfigExecutor().shutdown();
        }));

        ConfigManager configManager = new ConfigManager(ID).addCommon().addClient().addServer().makeConfig("custom", ConfigHelper.ConfigType.COMMON);
        configManager.DEBUG = getPlatform().isDevelopmentEnvironment();

        ConfigHolder common = configManager.getCommon();
        ConfigHolder client = configManager.getClient();
        ConfigHolder server = configManager.getServer();

        ConfigHolder custom = configManager.getConfig("custom");

        Map<String, String> map = new LinkedHashMap<>();
        map.put("key1", "val1");
        map.put("key2", "val2");
        map.put("key3", "val3");

        ConfigBool enabled = ConfigHelper.b("enabled", true);
        ConfigResourceLocation configResourceLocation = ConfigHelper.r("location", ResourceLocation.withDefaultNamespace("diamond"));
        Map<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("a", 1);
        linkedHashMap.put("b", 2);
        linkedHashMap.put("c", 3);
        linkedHashMap.put("d", 4);
        linkedHashMap.put("e", 5);
        linkedHashMap.put("f", 6);
        linkedHashMap.put("g", 7);
        linkedHashMap.put("h", 8);
        linkedHashMap.put("i", 9);
        linkedHashMap.put("j", 10);
        linkedHashMap.put("k", 11);
        linkedHashMap.put("l", 12);
        linkedHashMap.put("m", 13);
        linkedHashMap.put("n", 14);
        linkedHashMap.put("o", 15);
        linkedHashMap.put("p", 16);
        linkedHashMap.put("q", 17);
        linkedHashMap.put("r", 18);
        linkedHashMap.put("s", 19);
        linkedHashMap.put("t", 20);
        linkedHashMap.put("u", 21);
        linkedHashMap.put("v", 22);
        linkedHashMap.put("w", 23);
        linkedHashMap.put("x", 24);
        linkedHashMap.put("y", 26);
        linkedHashMap.put("z", 27);

        common
                .add(enabled)
                .add(ConfigHelper.s("credits", "made by ProstStuff"))
                .add(ConfigHelper.g("nestedGroup1")
                        .add(ConfigHelper.g("nestedGroup2")
                                .add(ConfigHelper.i("one", 10))
                                .add(ConfigHelper.d("two", 0.2))
                                .add(ConfigHelper.l("three", 24000))
                                .add(ConfigHelper.g("nestedGroup3")
                                        .add(ConfigHelper.i("min_one", 24))
                                        .add(ConfigHelper.d("six", -124))
                                        .add(ConfigHelper.l("seven", -24))
                                        .add(ConfigHelper.r("item", id("resource_location_id")))
                                        .add(ConfigHelper.m("map", linkedHashMap))
                                )
                        )
                        .add(ConfigHelper.a("listCompatiblity", 1, 2, 3, 4, 5, 6))
                        .add(ConfigHelper.m("evenMap", map))
                )
                .add(configResourceLocation)
                .add(ConfigHelper.u("uuid", UUID.nameUUIDFromBytes(ID.getBytes())))
                .add(ConfigHelper.e("enumIncluded", ConfigHelper.ConfigType.class, ConfigHelper.ConfigType.COMMON))
                .add(ConfigHelper.s("addedField", "Hello!"));

        client
                .add(ConfigHelper.b("client", true))
                .add(ConfigHelper.g("clientGroup1")
                        .add(ConfigHelper.g("clientGroup2")
                                .add(ConfigHelper.i("four", Integer.MIN_VALUE))
                                .add(ConfigHelper.d("five", Double.MIN_VALUE))
                                .add(ConfigHelper.l("six", Long.MAX_VALUE))
                        )
                );

        server
                .add(ConfigHelper.s("test", "This is server configuration!"));

        custom.add(ConfigHelper.s("about", "custom config name but with COMMON config type (works similar to common.json)"));
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

    public static IPlatformHelper getPlatform() {
        return Services.PLATFORM;
    }
}