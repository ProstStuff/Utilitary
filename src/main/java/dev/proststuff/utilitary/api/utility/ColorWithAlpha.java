package dev.proststuff.utilitary.api.utility;

import dev.proststuff.utilitary.api.impl.ColorARGB;
import net.minecraft.util.ARGB;

@SuppressWarnings("unused")
public class ColorWithAlpha extends Color implements ColorARGB {
    protected int alpha;
    protected int colorNoAlpha;

    public ColorWithAlpha(int alpha, int red, int green, int blue) {
        super(red, green, blue);
        this.alpha = alpha;
        refresh();
    }

    public ColorWithAlpha(int color) {
        this(ARGB.red(color), ARGB.green(color), ARGB.blue(color), ARGB.alpha(color));
    }

    public ColorWithAlpha(String hex) {
        this(Integer.parseInt(hex, 16));
    }

    public int getNoAlpha() {
        return colorNoAlpha;
    }

    @Override
    public void refresh() {
        super.refresh();
        this.colorNoAlpha = ARGB.color(red, green, blue);
    }

    @Override
    public int alpha() {
        return alpha;
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        refresh();
    }
}