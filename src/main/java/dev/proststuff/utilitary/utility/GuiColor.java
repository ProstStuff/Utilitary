package dev.proststuff.utilitary.utility;

import net.minecraft.util.Formatting;

/**
 * Color enum class similar to Minecraft Formatting but for rendering (draw text need alpha color)
 * @see Formatting
 */
public enum GuiColor {
    WHITE(Formatting.WHITE),
    BLACK(Formatting.BLACK),
    GRAY(Formatting.GRAY),
    DARK_GRAY(Formatting.DARK_GRAY),
    RED(Formatting.RED),
    DARK_RED(Formatting.DARK_RED),
    GREEN(Formatting.GREEN),
    DARK_GREEN(Formatting.DARK_GREEN),
    BLUE(Formatting.BLUE),
    DARK_BLUE(Formatting.DARK_BLUE),
    YELLOW(Formatting.YELLOW),
    GOLD(Formatting.GOLD),
    AQUA(Formatting.AQUA),
    DARK_AQUA(Formatting.DARK_AQUA),
    LIGHT_PURPLE(Formatting.LIGHT_PURPLE),
    DARK_PURPLE(Formatting.DARK_PURPLE),

    BACKGROUND(0xAA000000),
    FOREGROUND(0xCCFFFFFF),
    BORDER(0x55FFFFFF),
    SUCCESS(0x6600FF00),
    WARNING(0x66FFAA00),
    ERROR(0x66FF0000),
    DISABLED(0x55AAAAAA);

    private final int color;

    GuiColor(Formatting format) {
        Integer base = format.getColorValue();
        this.color = (base != null) ? 0xFF000000 | base : 0xFFFFFFFF;
    }

    GuiColor(int color) {
        this.color = color;
    }

    public int get() {
        return color;
    }

    public int withAlpha(int alpha) {
        return (alpha << 24) | (color & 0x00FFFFFF);
    }

    public int darker(float factor) {
        int r = (int) (((color >> 16) & 0xFF) * factor);
        int g = (int) (((color >> 8) & 0xFF) * factor);
        int b = (int) ((color & 0xFF) * factor);
        int a = (color >> 24) & 0xFF;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public int lighter(float factor) {
        int r = Math.min(255, (int) (((color >> 16) & 0xFF) / factor));
        int g = Math.min(255, (int) (((color >> 8) & 0xFF) / factor));
        int b = Math.min(255, (int) ((color & 0xFF) / factor));
        int a = (color >> 24) & 0xFF;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int rgba(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public int hover() {
        return lighter(1.2f);
    }

    public int pressed() {
        return darker(0.8f);
    }

    public int disabled() {
        return withAlpha(100);
    }
}