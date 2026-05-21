package dev.proststuff.utilitary.api.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class GraphicsUtils {
    public static Font font() {
        return Minecraft.getInstance().font;
    }

    public static void sprite(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, float offsetX, float offsetY, int width, int height, int textureWidth, int textureHeight, int color) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, offsetX, offsetY, width, height, textureWidth, textureHeight, color);
    }

    public static void sprite(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, float offsetX, float offsetY, int width, int height, int textureWidth, int textureHeight) {
        sprite(graphics, texture, x, y, offsetX, offsetY, width, height, textureWidth, textureHeight, 0xFFFFFFFF);
    }

    public static void sprite(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int width, int height, int textureWidth, int textureHeight, int color) {
        sprite(graphics, texture, x, y, 0, 0, width, height, textureWidth, textureHeight, color);
    }

    public static void sprite(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        sprite(graphics, texture, x, y, width, height, textureWidth, textureHeight, 0xFFFFFFFF);
    }

    public static void sprite(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int textureWidth, int textureHeight, int color) {
        sprite(graphics, texture, x, y, textureWidth, textureHeight, textureWidth, textureHeight, color);
    }

    public static void sprite(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int textureWidth, int textureHeight) {
        sprite(graphics, texture, x, y, textureWidth, textureHeight, 0xFFFFFFFF);
    }

    public static void text(GuiGraphicsExtractor graphics, Component text, float xAlignment, float yAlignment, int x, int y, int color, boolean shadow) {
        Font font = font();
        int height = font.lineHeight;
        int width = font.width(text);
        graphics.text(font, text, (int) (x - (width * xAlignment)), (int) (y - (height * yAlignment)), color, shadow);
    }

    public static void text(GuiGraphicsExtractor graphics, Component text, float xAlignment, float yAlignment, int x, int y, int color) {
        text(graphics, text, xAlignment, yAlignment, x, y, color, true);
    }

    public static void text(GuiGraphicsExtractor graphics, String text, float xAlignment, float yAlignment, int x, int y, int color) {
        text(graphics, Component.literal(text), xAlignment, yAlignment, x, y, color);
    }

    public static void rightText(GuiGraphicsExtractor graphics, Component text, float yAlignment, int x, int y, int color) {
        text(graphics, text, 1.0F, yAlignment, x, y, color);
    }

    public static void rightText(GuiGraphicsExtractor graphics, String text, float yAlignment, int x, int y, int color) {
        text(graphics, text, 1.0F, yAlignment, x, y, color);
    }

    public static void centerText(GuiGraphicsExtractor graphics, Component text, float yAlignment, int x, int y, int color) {
        text(graphics, text, 0.5F, yAlignment, x, y, color);
    }

    public static void centerText(GuiGraphicsExtractor graphics, String text, float yAlignment, int x, int y, int color) {
        text(graphics, text, 0.5F, yAlignment, x, y, color);
    }

    public static void leftText(GuiGraphicsExtractor graphics, Component text, float yAlignment, int x, int y, int color) {
        text(graphics, text, 0.0F, yAlignment, x, y, color);
    }

    public static void leftText(GuiGraphicsExtractor graphics, String text, float yAlignment, int x, int y, int color) {
        text(graphics, text, 0.0F, yAlignment, x, y, color);
    }

    public static void outline(GuiGraphicsExtractor graphics, int x, int y, int width, int height, int thickness, int color) {
        graphics.fill(x, y, x + width, y + thickness, color);
        graphics.fill(x, y + height - thickness, x + width, y + height, color);
        graphics.fill(x, y + thickness, x + thickness, y + height - thickness, color);
        graphics.fill(x + width - thickness, y + thickness, x + width, y + height - thickness, color);
    }

    public static void outline(GuiGraphicsExtractor graphics, int x, int y, int width, int height, int color) {
        outline(graphics, x, y, width, height, 1, color);
    }

    public static void textHighlight(GuiGraphicsExtractor graphics, int x0, int y0, int x1, int y1, int highlightColor, boolean invertText) {
        if (invertText) {
            graphics.fill(RenderPipelines.GUI_INVERT, x0, y0, x1, y1, -1);
        }

        graphics.fill(RenderPipelines.GUI_TEXT_HIGHLIGHT, x0, y0, x1, y1, highlightColor);
    }
}