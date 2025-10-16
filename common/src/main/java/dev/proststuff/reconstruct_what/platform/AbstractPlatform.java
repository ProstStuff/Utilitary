package dev.proststuff.reconstruct_what.platform;

import dev.proststuff.reconstruct_what.platform.registry.RegistryEntry;
import dev.proststuff.reconstruct_what.platform.registry.RegistryPlatform;
import dev.proststuff.reconstruct_what.utility.IFancyLogging;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public abstract class AbstractPlatform implements IFancyLogging {
    protected Map<Registry<?>, RegistryPlatform<?>> REGISTRIES = new HashMap<>();
    protected Logger LOGGER;
    protected boolean DEBUG = false;

    public abstract String getModId();
    public abstract ModPlatform getPlatform();
    public abstract boolean isModLoaded(String modId);
    public abstract boolean isDevelopmentEnvironment();
    public ModEnvironment getEnvironment() {return isDevelopmentEnvironment() ? ModEnvironment.DEVELOPMENT : ModEnvironment.PRODUCTION;}

    public <T> RegistryPlatform<T> getRegistry(Registry<T> registry) {return (RegistryPlatform<T>) REGISTRIES.get(registry);}
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

    public abstract <T> BiFunction<String, Supplier<? extends T>, Supplier<? extends T>> createRegistrationFunction(Registry<? super T> registry);
    public <T> RegistryEntry<T> register(String name, Registry<T> registry, Supplier<T> supplier) {return getRegistryOrThrow(registry).register(name, supplier);}
    // Minecraft's vanilla code has BlockEntitySupplier private, we make it platform dependent to return the builder.
    public abstract <B extends BlockEntity> BlockEntityType.Builder<B> blockEntityBuilder(BiFunction<BlockPos, BlockState, B> blockEntitySupplier, Block... validBlocks);

    public abstract <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T... messages);
    public abstract <T extends CustomPacketPayload> void sendToServer(T... messages);
    public <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T message) {sendToPlayer(player, message, null);}
    public <T extends CustomPacketPayload> void sendToServer(T message) {sendToServer(message, null);}

    @Override
    public boolean canPrint() {return DEBUG;}
    @Override
    public Logger getLogger() {
        if (this.LOGGER == null) this.LOGGER = LoggerFactory.getLogger(getModId());
        return this.LOGGER;
    }
}