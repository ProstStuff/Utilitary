package dev.proststuff.reconstruct_what;

import dev.proststuff.reconstruct_what.config.ConfigHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = ReconstructWhat.ID, value = Dist.CLIENT)
public class RWNeoForgeClientEvents {
    @SubscribeEvent
    public static void fmlClient(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ReconstructWhat.loadConfigs(ConfigHelper.ConfigType.CLIENT, null));
    }
}
