package dev.proststuff.utilitary.config.network;

import dev.proststuff.utilitary.Utilitary;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ServerBoundConfigSyncPacket(
        String configName,
        Identifier configFileIdentifier,
        int index,
        int total,
        String data,
        boolean isCompressed
) implements CustomPayload {
    public static final Id<ServerBoundConfigSyncPacket> ID =
            new Id<>(Identifier.of(Utilitary.ID, "config_sync"));

    public static final PacketCodec<ByteBuf, ServerBoundConfigSyncPacket> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            ServerBoundConfigSyncPacket::configName,
            Identifier.PACKET_CODEC,
            ServerBoundConfigSyncPacket::configFileIdentifier,
            PacketCodecs.INTEGER,
            ServerBoundConfigSyncPacket::index,
            PacketCodecs.INTEGER,
            ServerBoundConfigSyncPacket::total,
            PacketCodecs.STRING,
            ServerBoundConfigSyncPacket::data,
            PacketCodecs.BOOL,
            ServerBoundConfigSyncPacket::isCompressed,
            ServerBoundConfigSyncPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
