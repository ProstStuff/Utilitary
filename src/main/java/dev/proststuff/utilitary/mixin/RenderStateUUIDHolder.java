package dev.proststuff.utilitary.mixin;

import dev.proststuff.utilitary.api.impl.UUIDRenderStateHolder;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(EntityRenderState.class)
public class RenderStateUUIDHolder implements UUIDRenderStateHolder {
    @Unique private UUID utilitary$uuid = null;

    @Override
    public void utilitary$setUUID(UUID uuid) {
        if (utilitary$uuid == null && uuid != null) {
            this.utilitary$uuid = uuid;
        } else {
            throw new IllegalStateException("UUID can only be set once and not a null");
        }
    }

    @Override
    public UUID utilitary$getUUID() {
        return utilitary$uuid;
    }
}
