package dev.proststuff.reconstruct_what;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.proststuff.reconstruct_what.client.screen.RWConfigScreen;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import net.minecraft.network.chat.Component;

public class RWModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new RWConfigScreen(Component.literal(ReconstructWhat.NAME), parent, ConfigManager.getManager(ReconstructWhat.ID));
    }
}
