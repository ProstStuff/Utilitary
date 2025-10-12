package dev.proststuff.reconstruct_what.platform;

import dev.proststuff.reconstruct_what.platform.services.IPlatformHelper;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {return "NeoForge";}
    @Override
    public boolean isModLoaded(String modId) {return ModList.get().isLoaded(modId);}
    @Override
    public boolean isDevelopmentEnvironment() {return !FMLLoader.isProduction();}
    @SafeVarargs
    @Override
    public final <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T message, T... messages) {PacketDistributor.sendToPlayer(player, message, messages);}
}