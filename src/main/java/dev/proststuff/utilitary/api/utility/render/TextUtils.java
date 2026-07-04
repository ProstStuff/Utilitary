package dev.proststuff.utilitary.api.utility.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public class TextUtils extends GraphicsUtils {
    protected static Font font = Minecraft.getInstance().font;
    
    public static Font font() {
        return font;
    }

    public static void text(Component text, float xAlignment, float yAlignment, int x, int y, boolean shadow) {
        Font font = font();
        int height = font.lineHeight;
        int width = font.width(text);
        GRAPHICS.text(font, text, (int) (x - (width * xAlignment)), (int) (y - (height * yAlignment)), COLOR1, shadow);
    }

    public static void text(Component text, float xAlignment, float yAlignment, int x, int y) {
        text(text, xAlignment, yAlignment, x, y, true);
    }

    public static void text(String text, float xAlignment, float yAlignment, int x, int y) {
        text(Component.literal(text), xAlignment, yAlignment, x, y);
    }

    public static void rightText(Component text, float yAlignment, int x, int y) {
        text(text, 1.0F, yAlignment, x, y);
    }

    public static void rightText(String text, float yAlignment, int x, int y) {
        text(text, 1.0F, yAlignment, x, y);
    }

    public static void centerText(Component text, float yAlignment, int x, int y) {
        text(text, 0.5F, yAlignment, x, y);
    }

    public static void centerText(String text, float yAlignment, int x, int y) {
        text(text, 0.5F, yAlignment, x, y);
    }

    public static void leftText(Component text, float yAlignment, int x, int y) {
        text(text, 0.0F, yAlignment, x, y);
    }

    public static void leftText(String text, float yAlignment, int x, int y) {
        text(text, 0.0F, yAlignment, x, y);
    }


    public static void textHighlight(int x0, int y0, int x1, int y1, boolean invertText) {
        if (invertText) {
            GRAPHICS.fill(RenderPipelines.GUI_INVERT, x0, y0, x1, y1, -1);
        }

        GRAPHICS.fill(RenderPipelines.GUI_TEXT_HIGHLIGHT, x0, y0, x1, y1, COLOR1);
    }
}
