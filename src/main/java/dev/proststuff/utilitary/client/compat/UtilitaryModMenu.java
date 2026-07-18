package dev.proststuff.utilitary.client.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.proststuff.utilitary.client.UtilitaryConfigScreen;

public class UtilitaryModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return UtilitaryConfigScreen::new;
    }
}
