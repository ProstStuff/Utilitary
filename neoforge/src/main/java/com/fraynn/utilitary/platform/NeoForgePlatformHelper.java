package com.fraynn.utilitary.platform;

import com.fraynn.utilitary.platform.services.IPlatformHelper;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforgespi.language.IModInfo;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.Optional;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public @NonNull Platform getPlatform() {
        return Platform.NEOFORGE;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public @Nullable ModInfo getModInfo(String modId) {
        Optional<? extends ModContainer> container = ModList.get().getModContainerById(modId);

        if (container.isPresent()) {
            IModInfo modInfo = container.get().getModInfo();
            return new ModInfo(modInfo.getModId(), modInfo.getVersion().toString(), modInfo.getDisplayName(), modInfo.getDescription());
        }

        return null;
    }
}