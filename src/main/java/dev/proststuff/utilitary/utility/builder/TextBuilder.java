package dev.proststuff.utilitary.utility.builder;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Text builder utility inspired by Create's Lang class.
 */
public class TextBuilder {
    private final MutableText base;

    private TextBuilder(MutableText base) {
        this.base = base;
    }

    public static TextBuilder literal(String text) {
        return new TextBuilder(Text.literal(text));
    }

    public static TextBuilder translate(String key, Object... args) {
        return new TextBuilder(Text.translatable(key, args));
    }

    public TextBuilder append(String text) {
        base.append(Text.literal(text));
        return this;
    }

    public TextBuilder append(Text component) {
        base.append(component);
        return this;
    }

    public TextBuilder append(TextBuilder other) {
        base.append(other.build());
        return this;
    }

    public TextBuilder space() {
        base.append(Text.literal(" "));
        return this;
    }

    public TextBuilder newLine() {
        base.append(Text.literal("\n"));
        return this;
    }

    public TextBuilder style(Formatting... formats) {
        base.formatted(formats);
        return this;
    }

    public TextBuilder color(Formatting color) {
        base.formatted(color);
        return this;
    }

    public TextBuilder bold() {
        base.formatted(Formatting.BOLD);
        return this;
    }

    public TextBuilder italic() {
        base.formatted(Formatting.ITALIC);
        return this;
    }

    public TextBuilder gray() {
        base.formatted(Formatting.GRAY);
        return this;
    }

    public TextBuilder gold() {
        base.formatted(Formatting.GOLD);
        return this;
    }

    public TextBuilder green() {
        base.formatted(Formatting.GREEN);
        return this;
    }

    public TextBuilder darkGray() {
        base.formatted(Formatting.DARK_GRAY);
        return this;
    }

    public TextBuilder bracketed() {
        return new TextBuilder(Text.literal("[")
                .append(base.copy())
                .append(Text.literal("]")));
    }

    public TextBuilder parentheses() {
        return new TextBuilder(Text.literal("(")
                .append(base.copy())
                .append(Text.literal(")")));
    }

    public MutableText build() {
        return base;
    }
}