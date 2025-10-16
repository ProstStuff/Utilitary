package dev.proststuff.reconstruct_what.platform;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;

public class RWNeoForgePlatform extends RWAbstractPlatform {
    @Override
    public ModPlatform getPlatform() {
        return ModPlatform.NEOFORGE;
    }
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
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
}