package dev.proststuff.utilitary.api.utility.render;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public class GraphicsUtils {
    protected static GuiGraphicsExtractor GRAPHICS;
    protected static int COLOR1 = 0xFFFFFFFF;
    protected static int COLOR2 = 0xFFFFFFFF;

    public static void push(GuiGraphicsExtractor graphics) {
        GraphicsUtils.GRAPHICS = graphics;
    }

    public static void pop() {
        GraphicsUtils.GRAPHICS = null;
    }

    public static void resetColor1() {
        COLOR1 = 0xFFFFFFFF;
    }

    public static void resetColor2() {
        COLOR2 = 0xFFFFFFFF;
    }

    public static void setColor1(int color1) {
        GraphicsUtils.COLOR1 = color1;
    }

    public static void setColor2(int color2) {
        GraphicsUtils.COLOR2 = color2;
    }

    public static void outline(int x, int y, int width, int height, int thickness) {
        GRAPHICS.fill(x, y, x + width, y + thickness, COLOR1);
        GRAPHICS.fill(x, y + height - thickness, x + width, y + height, COLOR1);
        GRAPHICS.fill(x, y + thickness, x + thickness, y + height - thickness, COLOR1);
        GRAPHICS.fill(x + width - thickness, y + thickness, x + width, y + height - thickness, COLOR1);
    }

    public static void outline(int x0, int y0, int x1, int y1) {
        outline(x0, y0, x1 - x0, y1 - y0, 1);
    }
}