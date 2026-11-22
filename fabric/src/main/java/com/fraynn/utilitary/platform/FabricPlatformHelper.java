package com.fraynn.utilitary.platform;

import com.fraynn.utilitary.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.Optional;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public @NonNull Platform getPlatform() {
        return Platform.FABRIC;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public @Nullable ModInfo getModInfo(String modId) {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(modId);

        if (modContainer.isPresent()) {
            ModMetadata metadata = modContainer.get().getMetadata();
            return new ModInfo(metadata.getId(), metadata.getVersion().getFriendlyString(), metadata.getName(), metadata.getDescription());
        }

        return null;
    }
}
