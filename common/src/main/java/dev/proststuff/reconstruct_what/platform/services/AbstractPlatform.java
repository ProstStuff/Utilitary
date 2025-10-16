package dev.proststuff.reconstruct_what.platform.services;

import dev.proststuff.reconstruct_what.platform.ModPlatform;
import dev.proststuff.reconstruct_what.platform.registry.RegistryEntry;
import dev.proststuff.reconstruct_what.platform.registry.RegistryPlatform;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public abstract class AbstractPlatform implements IFancyLogging {
    protected Map<Registry<?>, RegistryPlatform<?>> REGISTRIES = new HashMap<>();
    protected Logger LOGGER;
    protected boolean DEBUG = false;

    public <T> RegistryPlatform<T> getRegistry(Registry<T> registry) {
        return (RegistryPlatform<T>) REGISTRIES.get(registry);
    }

    public <T> RegistryPlatform<T> getRegistryOrThrow(Registry<T> registry) {
        RegistryPlatform<T> registryPlatform = getRegistry(registry);
        if (registryPlatform == null) throw new NoSuchElementException("No such RegistryPlatform for " + registry.key().location() + " registry");
        return registryPlatform;
    }

    public <T> RegistryPlatform<T> addRegistryPlatform(RegistryPlatform<T> registryPlatform) {
        if (getRegistry(registryPlatform.getRegistry()) != null) throw new RuntimeException("RegistryPlatform for " + registryPlatform.getRegistry().key().location() + " already exist.");
        REGISTRIES.put(registryPlatform.getRegistry(), registryPlatform);
        return registryPlatform;
    }

    public <T> RegistryEntry<T> register(String name, Registry<T> registry, Supplier<T> supplier) {
        return getRegistryOrThrow(registry).register(name, supplier);
    }

    // Minecraft's vanilla code has BlockEntitySupplier private
    public abstract <T extends BlockEntity> CommonBlockEntityBuilder<T> blockEntityBuilder();

    public abstract <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T... messages);
    public abstract <T extends CustomPacketPayload> void sendToServer(T... messages);
    public <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T message) {sendToPlayer(player, message, null);}
    public <T extends CustomPacketPayload> void sendToServer(T message) {sendToServer(message, null);}

    public abstract String getModId();
    public abstract ModPlatform getPlatform();
    public abstract boolean isModLoaded(String modId);
    public abstract boolean isDevelopmentEnvironment();
    public String getEnvironmentName() {return isDevelopmentEnvironment() ? "development" : "production";}

    @Override
    public boolean canPrint() {return DEBUG;}
    @Override
    public Logger getLogger() {
        if (this.LOGGER == null) this.LOGGER = LoggerFactory.getLogger(getModId());
        return this.LOGGER;
    }

    public static class CommonBlockEntityBuilder<T extends BlockEntity> {
        private final Object internalBuilder;
        private final Supplier<BlockEntityType<T>> finalized;

        CommonBlockEntityBuilder(Object internalBuilder, Supplier<BlockEntityType<T>> finalized) {
            this.internalBuilder = internalBuilder;
            this.finalized = finalized;
        }

        public BlockEntityType<T> build() {
            return finalized.get();
        }

        Object getInternal() { return internalBuilder; }
    }
}