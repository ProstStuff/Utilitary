package dev.proststuff.utilitary.persistent;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

public class PersistentDataUtil {
    // Common side
    public static NbtCompound get(PlayerEntity player, String modId) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            return getServer(serverPlayer, modId);
        } else {
            return getClient(modId);
        }
    }

    private static NbtCompound getServer(ServerPlayerEntity player, String modId) {
        NbtCompound root = ((IPersistentData) player).utilitary$getPersistentDataSet();
        if (!root.contains(modId)) {
            root.put(modId, new NbtCompound());
        }
        return root.getCompound(modId);
    }

    public static void sendAllData(ServerPlayerEntity player) {
        NbtCompound data = ((IPersistentData) player).utilitary$getPersistentDataSet();
        ServerPlayNetworking.send(player, PersistentDataSyncPacket.send(data));
    }

    public static void sendModData(ServerPlayerEntity player, String modId) {
        NbtCompound data = getServer(player, modId);
        ServerPlayNetworking.send(player, PersistentDataSyncPacket.send(data));
    }

    // Client side
    @Environment(EnvType.CLIENT)
    private static final NbtCompound clientCache = new NbtCompound();

    @Environment(EnvType.CLIENT)
    private static NbtCompound getClient(String modId) {
        if (!clientCache.contains(modId)) {
            return new NbtCompound();
        }
        return clientCache.getCompound(modId);
    }

    // Manual data request
    @Environment(EnvType.CLIENT)
    public static void requestData() {
        ClientPlayNetworking.send(PersistentDataSyncPacket.request());
    }

    @Environment(EnvType.CLIENT)
    public static void updateClientCache(NbtCompound data) {
        clientCache.copyFrom(data);
    }
}