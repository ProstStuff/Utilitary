package dev.proststuff.utilitary.client;

import dev.proststuff.utilitary.Utilitary;
import dev.proststuff.utilitary.UtilitaryConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class UtilitaryConfigScreen extends OptionsSubScreen {
    public static final Component TITLE = Component.translatable("utilitary.config");

    private boolean debugPrinting = false;
    private boolean safeMode = false;

    public UtilitaryConfigScreen(Screen lastScreen) {
        super(lastScreen, Minecraft.getInstance().options, TITLE);
    }

    @Override
    protected void addOptions() {
        List<AbstractWidget> widgets = new ArrayList<>();

        UtilitaryConfig config = UtilitaryConfig.load();
        debugPrinting = config.debugLogging();
        safeMode = config.safeMode();

        AbstractButton button = CycleButton.onOffBuilder(config.debugLogging())
                .create(
                        Component.translatable("utilitary.config.debug_printing"),
                        (_, value) -> debugPrinting = value
                );

        widgets.add(button);
        button.setTooltip(Tooltip.create(Component.translatable("utilitary.config.debug_printing.info")));

        button = CycleButton.onOffBuilder(config.safeMode())
                .create(
                        Component.translatable("utilitary.config.safe_mode"),
                        (_, value) -> safeMode = value
                );

        widgets.add(button);
        button.setTooltip(Tooltip.create(Component.translatable("utilitary.config.safe_mode.info")));

        assert list != null;
        list.addSmall(widgets);
    }

    @Override
    public void onClose() {
        Utilitary.CONFIG = new UtilitaryConfig(debugPrinting, safeMode);
        UtilitaryConfig.save();
        super.onClose();
    }
}