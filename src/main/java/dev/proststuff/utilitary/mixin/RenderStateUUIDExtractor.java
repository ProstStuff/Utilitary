package dev.proststuff.utilitary.mixin;

import dev.proststuff.utilitary.api.client.entity.UUIDRenderStateHolder;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class RenderStateUUIDExtractor {
    @Inject(method = "extractRenderState", at = @At("HEAD"))
    public void utilitary$extractUUID(Entity entity, EntityRenderState state, float partialTicks, CallbackInfo ci) {
        ((UUIDRenderStateHolder) state).utilitary$setUUID(entity.getUUID());
    }
}
