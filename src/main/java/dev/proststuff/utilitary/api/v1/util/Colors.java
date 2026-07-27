package dev.proststuff.utilitary.api.v1.util;

public interface Colors {
    ColorRGB RED = new ColorRGB(0xFF0000).setImmutable();
    ColorRGB GREEN = new ColorRGB(0x00FF00).setImmutable();
    ColorRGB BLUE = new ColorRGB(0x0000FF).setImmutable();

    ColorRGB WHITE = new ColorRGB(0xFFFFFF).setImmutable();
    ColorRGB BLACK = new ColorRGB(0x000000).setImmutable();

    private static ColorRGB color(int hex) {
        return new ColorRGB(hex).setImmutable();
    }
}
