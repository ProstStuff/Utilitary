package dev.proststuff.reconstruct_what.platform;

import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to loadAll service for " + clazz.getName()));
        ReconstructWhat.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}