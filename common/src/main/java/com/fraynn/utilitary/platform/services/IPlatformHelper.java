package com.fraynn.utilitary.platform.services;

import com.fraynn.utilitary.platform.ModInfo;
import com.fraynn.utilitary.platform.ServiceUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;

public interface IPlatformHelper {
    IPlatformHelper INSTANCE = ServiceUtil.load(IPlatformHelper.class);
    RandomSource RANDOM = RandomSource.create();

    @NonNull Platform getPlatform();
    boolean isModLoaded(String modId);
    @Nullable ModInfo getModInfo(String modId);
    boolean isDevelopmentEnvironment();
    Path getConfigPath();

    default RandomSource getLibraryRandom() {
        return RANDOM;
    }

    default String getPlatformName() {
        return getPlatform().getSerializedName();
    }

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    enum Platform implements StringRepresentable {
        FABRIC("Fabric"),
        NEOFORGE("NeoForge");

        private final String name;

        Platform(String name) {
            this.name = name;
        }

        @Override
        public @NonNull String getSerializedName() {
            return name;
        }
    }
}