package dev.proststuff.reconstruct_what.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class RWFabricPlatform extends RWAbstractPlatform {
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
}