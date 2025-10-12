package dev.proststuff.reconstruct_what.client.screen;

import dev.proststuff.reconstruct_what.config.ConfigHelper;
import dev.proststuff.reconstruct_what.config.ConfigManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractConfigScreen extends Screen implements IHoldConfig<ConfigManager> {
    protected static final ResourceLocation INWORLD_MENU_BACKGROUND = ResourceLocation.withDefaultNamespace("textures/gui/inworld_menu_background.png");

    protected final ConfigManager manager;
    protected final Screen parent;

    protected PanoramaRenderer customPanorama;
    protected ResourceLocation customMenuBackground;
    protected ResourceLocation customInWorldMenuBackground;
    protected boolean renderPanorama = false;

    public AbstractConfigScreen(Component title, Screen parent, ConfigManager manager) {
        super(title);
        this.parent = parent;
        this.manager = manager;

        manager.loadSpecific(ConfigHelper.ConfigType.CLIENT, null, true);
        manager.loadSpecific(ConfigHelper.ConfigType.COMMON, null, true);
    }

    public AbstractConfigScreen setCustomPanorama(ResourceLocation cubeMap) {
        this.customPanorama = new PanoramaRenderer(new CubeMap(cubeMap));
        this.renderPanorama = true;
        return this;
    }

    public AbstractConfigScreen setCustomBackground(@Nullable ResourceLocation menu, @Nullable ResourceLocation inWorldMenu) {
        this.customMenuBackground = menu;
        this.customInWorldMenuBackground = inWorldMenu;
        return this;
    }

    public AbstractConfigScreen enablePanorama(boolean enable) {
        this.renderPanorama = enable;
        return this;
    }

    public boolean shouldRenderPanorama() {return this.renderPanorama;}
    public ConfigManager getConfig() {return this.manager;}

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (shouldRenderPanorama()) {
            renderPanorama(guiGraphics, partialTick);
        }

        renderBlurredBackground(partialTick);

        renderBeforeMenuBackground(guiGraphics, mouseX, mouseY, partialTick);

        renderMenuBackground(guiGraphics, 0, 0, this.width, this.height);

        renderAfterMenuBackground(guiGraphics, mouseX, mouseY, partialTick);
    }

    protected void renderBeforeMenuBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}
    protected void renderAfterMenuBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}

    @Override
    protected void renderPanorama(@NotNull GuiGraphics guiGraphics, float partialTick) {
        if (customPanorama != null) {
            this.customPanorama.render(guiGraphics, this.width, this.height, 1.0F, partialTick);
        } else {
            super.renderPanorama(guiGraphics, partialTick);
        }
    }

    @Override
    protected void renderMenuBackground(@NotNull GuiGraphics guiGraphics, int x, int y, int width, int height) {
        ResourceLocation menu = (customMenuBackground != null) ? customMenuBackground : MENU_BACKGROUND;
        ResourceLocation inWorldMenu = (customInWorldMenuBackground != null) ? customInWorldMenuBackground : INWORLD_MENU_BACKGROUND;

        ResourceLocation backgroundToUse = (this.minecraft != null && this.minecraft.level != null) ? menu : inWorldMenu;

        renderMenuBackgroundTexture(guiGraphics, backgroundToUse, x, y, 0.0F, 0.0F, width, height);
    }

    protected void renderBlurredBackground(float partialTick) {}

    public AbstractConfigScreen resetCustomBackground() {
        this.customMenuBackground = null;
        this.customInWorldMenuBackground = null;
        return this;
    }
}