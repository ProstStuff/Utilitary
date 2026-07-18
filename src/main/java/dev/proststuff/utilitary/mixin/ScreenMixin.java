package dev.proststuff.utilitary.mixin;

import dev.proststuff.utilitary.api.v1.client.gui.ScreenAutoParentable;
import dev.proststuff.utilitary.api.v1.client.gui.Tickable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow
    public abstract List<? extends GuiEventListener> children();

    @Inject(method = "tick", at = @At("TAIL"))
    public void utilitary$tick(CallbackInfo ci) {
        children().forEach(listener -> {
            if (listener instanceof Tickable tickable && tickable.canTick()) tickable.tick();
        });
    }

    @Inject(method = "addWidget", at = @At("TAIL"))
    public <T extends GuiEventListener & NarratableEntry> void utilitary$setParent(T widget, CallbackInfoReturnable<T> cir) {
        if (widget instanceof ScreenAutoParentable autoParented) {
            autoParented.setParent((Screen) (Object) this);
        }
    }
}
