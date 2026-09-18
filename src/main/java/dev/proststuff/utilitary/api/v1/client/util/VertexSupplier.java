package dev.proststuff.utilitary.api.v1.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;

public class VertexSupplier {
    protected int color = 0xFFFFFFFF;
    protected float u = 0.0F;
    protected float v = 0.0F;
    protected int overlay = OverlayTexture.NO_OVERLAY;
    protected int light = LightCoordsUtil.FULL_BRIGHT;
    protected float normalX = 0.0F;
    protected float normalY = 0.0F;
    protected float normalZ = 0.0F;

    protected PoseStack.Pose pose = null;
    protected VertexConsumer buffer = null;

    public VertexSupplier() {}

    public VertexSupplier reset() {
        this.color = 0xFFFFFFFF;
        this.u = 0.0F;
        this.v = 0.0F;
        this.overlay = OverlayTexture.NO_OVERLAY;
        this.light = LightCoordsUtil.FULL_BRIGHT;
        this.normalX = 0.0F;
        this.normalY = 0.0F;
        this.normalZ = 0.0F;
        return this;
    }

    public VertexSupplier color(int color) {
        this.color = color;
        return this;
    }

    public VertexSupplier overlay(int overlay) {
        this.overlay = overlay;
        return this;
    }

    public VertexSupplier light(int light) {
        this.light = light;
        return this;
    }

    public VertexSupplier uv(float u, float v) {
        this.u = u;
        this.v = v;
        return this;
    }

    public VertexSupplier normal(float x, float y, float z) {
        this.normalX = x;
        this.normalY = y;
        this.normalZ = z;
        return this;
    }

    public VertexSupplier emit(float x, float y, float z) {
        if (this.pose == null || this.buffer == null) throw new IllegalStateException("Can't create vertex before push!");
        buffer
                .addVertex(pose.pose(), x, y, z)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(pose, normalX, normalY, normalZ);
        return this;
    }

    public VertexSupplier push(PoseStack.Pose pose, VertexConsumer buffer) {
        if (this.pose != null || this.buffer != null) throw new IllegalStateException("Last push has not been properly popped yet!");
        this.pose = pose;
        this.buffer = buffer;
        return this;
    }

    public VertexSupplier pop() {
        if (this.pose == null || this.buffer == null) throw new IllegalStateException("Already popped!");
        this.pose = null;
        this.buffer = null;
        return this;
    }
}
