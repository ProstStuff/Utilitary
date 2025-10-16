package dev.proststuff.reconstruct_what.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public abstract class AbstractNeoForgePlatform extends AbstractPlatform {
    protected IEventBus eventBus;
    protected Map<Registry<?>, DeferredRegister<?>> DEFERRED_REGISTERS = new HashMap<>();

    public void setEventBus(IEventBus eventBus) {this.eventBus = eventBus;}
    public IEventBus getEventBus() {return eventBus;}

    public <T> DeferredRegister<T> getDeferredRegister(Registry<T> registry) {
        DEFERRED_REGISTERS.computeIfAbsent(registry, r -> {
            DeferredRegister<T> register = DeferredRegister.create(registry, this.getModId());

            if (eventBus != null) {
                register.register(eventBus);
            }

            return register;
        });

        return (DeferredRegister<T>) DEFERRED_REGISTERS.get(registry);
    }

    @Override
    public <T> BiFunction<String, Supplier<? extends T>, Supplier<? extends T>> createRegistrationFunction(Registry<? super T> registry) {return getDeferredRegister(registry)::register;}
    @Override
    public ModPlatform getPlatform() {return ModPlatform.NEOFORGE;}
    @Override
    public boolean isModLoaded(String modId) {return ModList.get().isLoaded(modId);}
    @Override
    public boolean isDevelopmentEnvironment() {return !FMLLoader.isProduction();}

    @SafeVarargs
    @Override
    public final <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T... messages) {
        for (T message : messages) {
            if (message == null) continue;

            PacketDistributor.sendToPlayer(player, message);
        }
    }
    @SafeVarargs
    @Override
    public final <T extends CustomPacketPayload> void sendToServer(T... messages) {
        for (T message : messages) {
            if (message == null) continue;

            PacketDistributor.sendToServer(message);
        }
    }
    @Override
    public <B extends BlockEntity> BlockEntityType.Builder<B> blockEntityBuilder(BiFunction<BlockPos, BlockState, B> blockEntitySupplier, Block... validBlocks) {return BlockEntityType.Builder.of(blockEntitySupplier::apply, validBlocks);}
}