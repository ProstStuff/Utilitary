package dev.proststuff.reconstruct_what;

import dev.proststuff.reconstruct_what.client.screen.RWConfigScreen;
import dev.proststuff.reconstruct_what.config.ConfigHelper;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = ReconstructWhat.ID, value = Dist.CLIENT)
@Mod(value = ReconstructWhat.ID, dist = Dist.CLIENT)
public class RWNeoForgeClient {
    public RWNeoForgeClient(ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (c, s) -> new RWConfigScreen(Component.literal(ReconstructWhat.NAME), s, ConfigManager.getManager(ReconstructWhat.ID)));
    }

    @SubscribeEvent
    public static void fmlClient(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ReconstructWhat.loadConfigs(ConfigHelper.ConfigType.CLIENT, null));
    }
}
