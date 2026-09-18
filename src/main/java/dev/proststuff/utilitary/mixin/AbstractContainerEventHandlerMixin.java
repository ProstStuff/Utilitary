package dev.proststuff.utilitary.mixin;

import dev.proststuff.utilitary.api.v1.client.gui.Focusable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerEventHandler.class)
public class AbstractContainerEventHandlerMixin {
    @Inject(method = "setFocused", at = @At("HEAD"), cancellable = true)
    private void setFocused(GuiEventListener focused, CallbackInfo ci) {
        if (focused instanceof Focusable focusable && !focusable.isFocusable()) ci.cancel();
    }
}
