package dev.proststuff.reconstruct_what.platform;

import dev.proststuff.reconstruct_what.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @SafeVarargs
    @Override
    public final <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T message, T... messages) {
        ServerPlayNetworking.send(player, message);

        for (T msg : messages) {
            ServerPlayNetworking.send(player, msg);
        }
    }
}
