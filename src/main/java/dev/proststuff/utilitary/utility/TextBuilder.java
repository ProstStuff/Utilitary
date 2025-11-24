package dev.proststuff.utilitary.utility;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Text builder utility inspired by Create's Lang class.
 */
public class TextBuilder {
    private final MutableText base;
    private int indents = 0;

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

    public TextBuilder indent() {
        return indent(true);
    }

    public TextBuilder indent(boolean count) {
        space();
        if (count) {
            this.indents++;
        }
        return this;
    }

    public TextBuilder resetIndent() {
        this.indents = 0;
        return this;
    }

    public TextBuilder newLine() {
        base.append(Text.literal("\n"));
        for (int i = 0; i < indents; i++) {
            indent(false);
        }
        return this;
    }

    public TextBuilder format(Formatting... formats) {
        base.formatted(formats);
        return this;
    }

    public TextBuilder color(Formatting color) {
        base.formatted(color);
        return this;
    }

    public TextBuilder obfuscated() {
        base.formatted(Formatting.OBFUSCATED);
        return this;
    }

    public TextBuilder bold() {
        base.formatted(Formatting.BOLD);
        return this;
    }

    public TextBuilder strikethrough() {
        base.formatted(Formatting.STRIKETHROUGH);
        return this;
    }

    public TextBuilder underline() {
        base.formatted(Formatting.UNDERLINE);
        return this;
    }

    public TextBuilder italic() {
        base.formatted(Formatting.ITALIC);
        return this;
    }

    public TextBuilder reset() {
        this.format(Formatting.RESET);
        return this;
    }

    public TextBuilder prefix(String prefix) {
        return new TextBuilder(Text.literal(prefix)
                .append(base.copy()));
    }

    public TextBuilder suffix(String suffix) {
        return new TextBuilder(base.copy().append(Text.literal(suffix)));
    }

    public TextBuilder between(String prefix, String suffix) {
        return new TextBuilder(Text.literal(prefix)
                .append(base.copy())
                .append(Text.literal(suffix)));
    }

    public TextBuilder bracketed() {
        return between("[", "]");
    }

    public TextBuilder parentheses() {
        return between("(", ")");
    }

    public MutableText build() {
        return base;
    }
}