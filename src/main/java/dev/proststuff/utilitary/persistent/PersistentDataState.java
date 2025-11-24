package dev.proststuff.utilitary.persistent;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PersistentDataState extends PersistentState {
    // <Player UUID, <ModID, Data>>
    public Map<UUID, Map<String, NbtCompound>> data = new HashMap<>();

    public PersistentDataState() {}

    public PersistentDataState(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (nbt.contains("PersistedData", NbtElement.LIST_TYPE)) {
            NbtList list = nbt.getList("PersistedData", NbtElement.COMPOUND_TYPE);

            for (NbtElement rootElement : list) {
                NbtCompound rootData = (NbtCompound) rootElement;
                UUID playerUUID = rootData.getUuid("PlayerUUID");
                NbtList stored = rootData.getList("Stored", NbtElement.COMPOUND_TYPE);

                for (NbtElement element : stored) {
                    NbtCompound modData = (NbtCompound) element;
                    String modId = modData.getString("Mod");
                    modData.remove("Mod");
                    get(playerUUID, modId).copyFrom(modData);
                }
            }
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList list = new NbtList();

        data.forEach(
                (uuid, mods) -> {
                    NbtCompound compound = new NbtCompound();
                    NbtList stored = new NbtList();
                    compound.putUuid("PlayerUUID", uuid);
                    mods.forEach((modId, original) -> {
                        NbtCompound copy = original.copy();
                        copy.putString("Mod", modId);
                        stored.add(copy);
                    });

                    compound.put("Data", stored);
                    list.add(compound);
                }
        );

        nbt.put("PersistedData", list);
        return nbt;
    }

    public NbtCompound get(PlayerEntity player, String modId) {
        return get(player.getUuid(), modId);
    }

    public NbtCompound get(UUID uuid, String modId) {
        Map<String, NbtCompound> modDataStorage = data.computeIfAbsent(uuid, (i) -> new HashMap<>());
        return modDataStorage.computeIfAbsent(modId, (string) -> new NbtCompound());
    }

    public void send(ServerPlayerEntity serverPlayer) {
        NbtList toSend = new NbtList();

        if (data.containsKey(serverPlayer.getUuid())) {
            data.get(serverPlayer.getUuid()).forEach((modId, compound) -> {
                NbtCompound copy = compound.copy();
                copy.putString("Mod", modId);
                toSend.add(copy);
            });
        }

        NbtCompound c = new NbtCompound();
        c.put("Data", toSend);
        ServerPlayNetworking.send(serverPlayer, new PersistentDataSyncPacket(c));
    }
}