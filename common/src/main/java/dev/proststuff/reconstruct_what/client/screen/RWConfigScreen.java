package dev.proststuff.reconstruct_what.client.screen;

import dev.proststuff.reconstruct_what.RWConfigExample;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RWConfigScreen extends AbstractConfigScreen {
    private static final ResourceLocation ICON = ResourceLocation.withDefaultNamespace("textures/gui/sprites/icon/info.png");
    private static final int ICON_SIZE = 20;
    public static List<String> texts = List.of(
            "RECONSTRUCT WHAT?",
            "THERE'S NOTHING LEFT!",
            "A JSON-based config library",
            "(config screen coming soon!)",
            "",
            "config path is located at:"
    );

    public RWConfigScreen(Component title, Screen parent, ConfigManager manager) {
        super(title, parent, manager);
    }

    @Override
    protected void init() {
        super.init();
        texts = RWConfigExample.CONFIG_SCREEN_MESSAGES.get();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        Font font = Minecraft.getInstance().font;

        int centerW = width / 2;
        int centerH = height / 2;
        int s = ICON_SIZE / 2;

        guiGraphics.blit(ICON, centerW - s, centerH - s - (font.lineHeight * 2),
                0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

        for (int i = 0; i < texts.size(); i++) {
            String text = texts.get(i);

            if (text.isEmpty()) continue;

            guiGraphics.drawCenteredString(font,
                    text,
                    centerW,
                    centerH + (font.lineHeight * i),
                    0xffffff);
        }

        guiGraphics.drawCenteredString(font,
                manager.getConfigPath().toAbsolutePath().toString(),
                centerW,
                centerH + (font.lineHeight * texts.size()),
                0xffffff);
    }
}
