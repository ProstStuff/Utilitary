package dev.proststuff.utilitary.api.v1.client.renderer;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public class CustomRenderTypes {
    public static final Function<Identifier, RenderType> EYES_NO_CULL = Util.memoize(texture ->
            RenderType.create("eyes_no_cull", RenderSetup.builder(CustomRenderPipelines.EYES_NO_CULL)
                    .withTexture("Sampler0", texture)
                    .sortOnUpload()
                    .createRenderSetup())
    );

    public static RenderType eyesNoCull(Identifier texture) {
        return EYES_NO_CULL.apply(texture);
    }
}