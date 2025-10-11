package dev.proststuff.reconstruct_what;

import dev.proststuff.reconstruct_what.client.screen.RWConfigScreen;
import dev.proststuff.reconstruct_what.config.ConfigHelper;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import dev.proststuff.reconstruct_what.config.instance.ConfigCodecs;
import dev.proststuff.reconstruct_what.config.instance.ConfigGroup;
import dev.proststuff.reconstruct_what.config.instance.ConfigHolder;
import dev.proststuff.reconstruct_what.config.instance.value.*;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * RECONSTRUCT WHAT
 * Config (Common, Client, Server) example
 */
public class RWConfigExample {
    public static final ConfigManager MANAGER;

    public static final ConfigHolder STARTUP;
    public static final ConfigHolder COMMON;
    public static final ConfigHolder CLIENT;
    public static final ConfigHolder CUSTOM;

    public static final ConfigList<String> RECONSTRUCT_WHAT;

    public static final ConfigBool COMMON_ENABLED;
    public static final ConfigInt INT_NUMBER;
    public static final ConfigDouble DOUBLE_NUMBER;
    public static final ConfigString STR;

    public static final ConfigList<String> CONFIG_SCREEN_MESSAGES;
    public static final ConfigList<ResourceLocation> CONFIG_LIST_RL;
    public static final ConfigMap<String, String> CONFIG_MAP_SS;
    public static final ConfigMap<UUID, Double> CONFIG_MAP;

    public static final ConfigColor COLOR;
    public static final ConfigResourceLocation LOC;
    public static final ConfigUUID UUID_VALUE;
    public static final ConfigVec2 VEC2;
    public static final ConfigVec3 VEC3;

    static {
        // Sample maps
        Map<String, String> exs = new LinkedHashMap<>();
        exs.put("this", "a");
        exs.put("map", "b");
        exs.put("has", "c");
        exs.put("both codecs", "d");

        Map<UUID, Double> ns = new LinkedHashMap<>();
        ns.put(UUID.nameUUIDFromBytes("ProstStuff".getBytes()), 0.1);
        ns.put(UUID.nameUUIDFromBytes("Arthritis always come back".getBytes()), -0.1);

        MANAGER = new ConfigManager(ReconstructWhat.ID, true, true, true, true)
                .makeConfig("minecraft", ConfigHelper.ConfigType.COMMON);
        MANAGER.DEBUG = true;

        RECONSTRUCT_WHAT = new ConfigList<>("unknown", List.of(
                "It is so dead as it possibly could ever be,",
                "and people are still like",
                "Maybe it is alive,",
                "though,",
                "maybe it comes back,",
                "maybe someone,",
                "like,",
                "reconstructs his like-",
                "",
                "RECONSTRUCT WHAT?!",
                "THERE IS NOTHING LEFT"
        ), ConfigCodecs.STRING);

        // Make sure you have `startup` config created
        STARTUP = MANAGER.getStartup().add(RECONSTRUCT_WHAT);

        // Register listener
        STARTUP.onLoaded(manager -> {
            for (String string : RECONSTRUCT_WHAT.get()) {
                manager.info(IFancyLogging.LogType.ACTION, string);
            }
        });

        MANAGER.loadSpecific(ConfigHelper.ConfigType.STARTUP, null, true);

        COMMON_ENABLED = new ConfigBool("bool", true);
        INT_NUMBER = new ConfigInt("integer", 123);
        DOUBLE_NUMBER = new ConfigDouble("double", Math.PI);
        STR = new ConfigString("str", "open minecraft.json to see more");

        COMMON = MANAGER.getCommon()
                .add(COMMON_ENABLED)
                .add(INT_NUMBER)
                .add(DOUBLE_NUMBER)
                .add(STR);

        CONFIG_SCREEN_MESSAGES = new ConfigList<>("configScreenMessages", RWConfigScreen.texts, ConfigCodecs.STRING);
        CONFIG_LIST_RL = new ConfigList<>("listWithCustomElementCodec", List.of(
                BuiltInRegistries.ITEM.getKey(Items.DIAMOND_PICKAXE),
                BuiltInRegistries.BLOCK.getKey(Blocks.DIRT)
        ), ConfigCodecs.RESOURCE_LOCATION);
        CONFIG_MAP_SS = new ConfigMap<>("mapStrings", exs, ConfigCodecs.STRING, ConfigCodecs.STRING);
        CONFIG_MAP = new ConfigMap<>("entirelyDifferentCodec", ns, ConfigCodecs.UUID, ConfigCodecs.DOUBLE);

        CLIENT = MANAGER.getClient()
                .add(CONFIG_SCREEN_MESSAGES)
                .add(CONFIG_LIST_RL)
                .add(CONFIG_MAP_SS)
                .add(CONFIG_MAP);

        COLOR = new ConfigColor("color", new Color(12, 24, 25));
        LOC = new ConfigResourceLocation("location", ReconstructWhat.id("there_is_nothing_left"));
        UUID_VALUE = new ConfigUUID("uuid", UUID.nameUUIDFromBytes(ReconstructWhat.ID.getBytes()));
        VEC2 = new ConfigVec2("vector2");
        VEC3 = new ConfigVec3("vector3", new Vec3(1, 2, 3), true); // runtime-only

        CUSTOM = MANAGER.getConfig("minecraft")
                .add(LOC)
                .add(UUID_VALUE)
                .add(VEC2)
                .add(VEC3)
                .add(COLOR);

        MANAGER.getServer()
                .add(new ConfigGroup("group1")
                        .add(new ConfigString("this", "is"))
                        .add(new ConfigString("a", "group"))
                        .add(new ConfigGroup("group2")
                                .add(new ConfigString("another", "one"))
                                .add(new ConfigInt("one", 1))
                                .add(new ConfigDouble("zero.two", 0.2))
                                .add(new ConfigGroup("moreGroups")
                                        .add(new ConfigGroup("andSoOn"))))
                );

    }

    public static void init() {
        // no-op; static block already did everything
    }
}
