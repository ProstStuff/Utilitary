package dev.proststuff.reconstruct_what;


import dev.proststuff.reconstruct_what.config.ClientBoundConfigSyncPacket;
import dev.proststuff.reconstruct_what.config.ConfigHelper;
import dev.proststuff.reconstruct_what.config.instance.ConfigHolder;
import dev.proststuff.reconstruct_what.utility.RWEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;

@Mod(ReconstructWhat.ID)
@EventBusSubscriber(modid = ReconstructWhat.ID)
public class ReconstructWhatNeoForge {
    public ReconstructWhatNeoForge(IEventBus eventBus) {
        ReconstructWhat.init();
    }

    @SubscribeEvent
    public static void fmlSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> ReconstructWhat.loadConfigs(ConfigHelper.ConfigType.COMMON, null));
    }

    @SubscribeEvent
    public static void fmlServerStop(ServerStoppingEvent event) {
        ConfigHolder.stopWatching(ConfigHelper.ConfigType.SERVER);
    }

    @SubscribeEvent
    public static void fmlServer(ServerAboutToStartEvent event) {
        ReconstructWhat.loadConfigs(ConfigHelper.ConfigType.SERVER, event.getServer());
    }

    @SubscribeEvent
    public static void playerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            RWEvents.playerJoined(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void registerPayload(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToClient(ClientBoundConfigSyncPacket.TYPE, ClientBoundConfigSyncPacket.STREAM_CODEC, new MainThreadPayloadHandler<>((packet, ctx) -> {
                    ctx.enqueueWork(() -> packet.handle((ClientLevel) ctx.player().level(), Minecraft.getInstance()));
                }));
    }
}