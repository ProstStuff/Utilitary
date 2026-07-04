package dev.proststuff.utilitary.api.utility.render;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class SpriteUtils extends GraphicsUtils {
    public static void sprite(Identifier texture, int x, int y, float offsetX, float offsetY, int width, int height, int textureWidth, int textureHeight) {
        GRAPHICS.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, offsetX, offsetY, width, height, textureWidth, textureHeight, COLOR1);
    }

    public static void sprite(Identifier texture, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        sprite(texture, x, y, 0, 0, width, height, textureWidth, textureHeight);
    }

    public static void sprite(Identifier texture, int x, int y, int textureWidth, int textureHeight) {
        sprite(texture, x, y, textureWidth, textureHeight, textureWidth, textureHeight);
    }
}
