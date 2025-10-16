package dev.proststuff.reconstruct_what.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class AbstractFabricPlatform extends AbstractPlatform {
    @Override
    public <T> BiFunction<String, Supplier<? extends T>, Supplier<? extends T>> createRegistrationFunction(Registry<? super T> registry) {
        return (name, supplier) -> {
            T o = Registry.register(registry, ResourceLocation.fromNamespaceAndPath(this.getModId(), name), supplier.get());
            return () -> o;
        };
    }

    @Override
    public ModPlatform getPlatform() {return ModPlatform.FABRIC;}
    @Override
    public boolean isModLoaded(String modId) {return FabricLoader.getInstance().isModLoaded(modId);}
    @Override
    public boolean isDevelopmentEnvironment() {return FabricLoader.getInstance().isDevelopmentEnvironment();}

    @SafeVarargs
    @Override
    public final <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T... messages) {
        for (T msg : messages) {
            if (msg == null) continue;
            ServerPlayNetworking.send(player, msg);
        }
    }
    @SafeVarargs
    @Override
    public final <T extends CustomPacketPayload> void sendToServer(T... messages) {
        for (T msg : messages) {
            if (msg == null) continue;
            ClientPlayNetworking.send(msg);
        }
    }

    @Override
    public <B extends BlockEntity> BlockEntityType.Builder<B> blockEntityBuilder(BiFunction<BlockPos, BlockState, B> blockEntitySupplier, Block... validBlocks) {
        return BlockEntityType.Builder.of(blockEntitySupplier::apply, validBlocks);
    }
}