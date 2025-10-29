package dev.proststuff.utilitary.persistent;

import dev.proststuff.utilitary.Utilitary;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PersistentDataSyncPacket(String modId, NbtCompound data) implements CustomPayload {
    public static final CustomPayload.Id<PersistentDataSyncPacket> ID = new CustomPayload.Id<>(Identifier.of(Utilitary.ID, "persistent_data_sync"));
    public static final PacketCodec<RegistryByteBuf, PersistentDataSyncPacket> PACKET_CODEC =PacketCodec.of(
            (packet, buf) -> buf.writeString(packet.modId).writeNbt(packet.data),
            (buf) -> new PersistentDataSyncPacket(buf.readString(), buf.readNbt())
    );

    public static PersistentDataSyncPacket request() {
        return new PersistentDataSyncPacket(Utilitary.ID, new NbtCompound());
    }

    public static PersistentDataSyncPacket sendAll(NbtCompound data) {
        return new PersistentDataSyncPacket(Utilitary.ID, data);
    }

    public static PersistentDataSyncPacket sendForMod(NbtCompound modData, String modId) {
        return new PersistentDataSyncPacket(modId, modData);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}