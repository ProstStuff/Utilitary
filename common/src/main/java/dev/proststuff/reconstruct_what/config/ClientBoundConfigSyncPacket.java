package dev.proststuff.reconstruct_what.config;

import dev.proststuff.reconstruct_what.ReconstructWhat;
import dev.proststuff.reconstruct_what.client.ClientConfigSync;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ClientBoundConfigSyncPacket(
        String modId,
        String configName,
        int index,
        int total,
        String data,
        boolean isCompressed
) implements CustomPacketPayload {
    public static final Type<ClientBoundConfigSyncPacket> TYPE =
            new Type<>(ReconstructWhat.id("config_sync"));
    public static final StreamCodec<ByteBuf, ClientBoundConfigSyncPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ClientBoundConfigSyncPacket::modId,
            ByteBufCodecs.STRING_UTF8,
            ClientBoundConfigSyncPacket::configName,
            ByteBufCodecs.INT,
            ClientBoundConfigSyncPacket::index,
            ByteBufCodecs.INT,
            ClientBoundConfigSyncPacket::total,
            ByteBufCodecs.STRING_UTF8,
            ClientBoundConfigSyncPacket::data,
            ByteBufCodecs.BOOL,
            ClientBoundConfigSyncPacket::isCompressed,
            ClientBoundConfigSyncPacket::new
    );

    public void handle(ClientLevel level, Minecraft mc) {
        ClientConfigSync.receiveChunk(this);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}