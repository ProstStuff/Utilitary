package dev.proststuff.utilitary.api.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public enum ContentAlignment {
    TOP {
        @Override
        public int fromLeft(int anchor, int width) {
            return anchor;
        }

        @Override
        public int fromTop(int anchor, int height) {
            return anchor;
        }
    },
    LEFT {
        @Override
        public int fromLeft(int anchor, int width) {
            return anchor;
        }

        @Override
        public int fromTop(int anchor, int height) {
            return anchor;
        }
    },
    CENTER {
        @Override
        public int fromLeft(int anchor, int width) {
            return anchor - width / 2;
        }

        @Override
        public int fromTop(int anchor, int height) {
            return anchor - height / 2;
        }
    },
    RIGHT {
        @Override
        public int fromLeft(int anchor, int width) {
            return anchor + width;
        }

        @Override
        public int fromTop(int anchor, int height) {
            return anchor;
        }
    },
    BOTTOM {
        @Override
        public int fromLeft(int anchor, int width) {
            return anchor;
        }

        @Override
        public int fromTop(int anchor, int height) {
            return anchor + height;
        }
    };

    public abstract int fromLeft(int anchor, int width);
    public abstract int fromTop(int anchor, int height);

    public int fromTextLeft(int anchor, Font font, Component text) {
        return fromLeft(anchor, font.width(text));
    }

    public int fromTextTop(int anchor, Font font, Component text, int width) {
        return fromTop(anchor, width > 0 ? font.wordWrapHeight(text, width) : font.lineHeight);
    }
}