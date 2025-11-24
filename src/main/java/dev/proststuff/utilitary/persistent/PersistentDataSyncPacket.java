package dev.proststuff.utilitary.persistent;

import dev.proststuff.utilitary.Utilitary;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PersistentDataSyncPacket(NbtCompound data) implements CustomPayload {
    public static final CustomPayload.Id<PersistentDataSyncPacket> ID = new CustomPayload.Id<>(Identifier.of(Utilitary.ID, "persistent_data_sync"));
    public static final PacketCodec<RegistryByteBuf, PersistentDataSyncPacket> PACKET_CODEC = PacketCodec.of(
            (packet, buf) -> buf.writeNbt(packet.data),
            (buf) -> new PersistentDataSyncPacket(buf.readNbt())
    );

    public static PersistentDataSyncPacket request() {
        return new PersistentDataSyncPacket(new NbtCompound());
    }

    public static PersistentDataSyncPacket send(NbtCompound data) {
        return new PersistentDataSyncPacket(data);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}