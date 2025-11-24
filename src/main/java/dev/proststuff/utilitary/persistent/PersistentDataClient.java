package dev.proststuff.utilitary.persistent;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PersistentDataClient {
    public static Map<String, NbtCompound> cache = new HashMap<>();

    public static void request() {
        ClientPlayNetworking.send(PersistentDataSyncPacket.request());
    }

    public static NbtCompound get(String modId) {
        return cache.computeIfAbsent(modId, (id) -> new NbtCompound());
    }

    public static void update(PersistentDataSyncPacket packet) {
        NbtCompound root = packet.data();

        if (root.contains("Data")) {
            for (NbtElement element : root.getList("Data", NbtElement.COMPOUND_TYPE)) {
                NbtCompound modData = (NbtCompound) element;
                String modId = modData.getString("Mod");
                modData.remove("Mod");
                get(modId).copyFrom(modData);
            }
        }
    }
}
