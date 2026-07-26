package dev.proststuff.utilitary.api.v1.client.gui;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class MultiFocusScreen extends Screen {
    public static final Identifier DEFAULT_GROUP = Identifier.withDefaultNamespace("default");

    protected Map<Identifier, GuiEventListener> focusedWidgets = new HashMap<>();

    protected MultiFocusScreen(Component title) {
        super(title);
    }

    public @Nullable GuiEventListener getFocused(Identifier group) {
        return focusedWidgets.get(group);
    }

    public void setFocused(@NonNull Identifier group, @Nullable GuiEventListener focused) {
        GuiEventListener lastFocused = getFocused(group);

        if (lastFocused != focused) {
            if (lastFocused != null) {
                lastFocused.setFocused(false);
            }

            if (focused != null) {
                focused.setFocused(true);
            }

            focusedWidgets.put(group, focused);
        }
    }

    public void setFocused(Identifier group, boolean focused) {
        if (!focused) {
            setFocused(group, null);
        }
    }

    @Deprecated
    @Override
    public @Nullable GuiEventListener getFocused() {
        return getFocused(DEFAULT_GROUP);
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        if (focused instanceof GroupableElement element) {
            setFocused(element.getGroup(), focused);
        } else {
            setFocused(DEFAULT_GROUP, focused);
        }
    }
}
