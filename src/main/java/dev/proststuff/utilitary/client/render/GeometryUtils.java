package dev.proststuff.utilitary.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.Direction;

@Environment(EnvType.CLIENT)
public interface GeometryUtils {
    static void cube(PoseStack poseStack, SubmitNodeCollector collector, RenderType renderType, float width, float height, float depth, int light, int overlay, int color) {
        collector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
            float x = width / 2.0F;
            float y = height / 2.0F;
            float z = depth / 2.0F;

            quad(pose, buffer,
                    -x, -y, z,
                    x, -y, z,
                    x,  y, z,
                    -x,  y, z,
                    0, 0, 1,
                    light, overlay, color
            );

            quad(pose, buffer,
                    x, -y, -z,
                    -x, -y, -z,
                    -x,  y, -z,
                    x,  y, -z,
                    0, 0, -1,
                    light, overlay, color
            );

            quad(
                    pose, buffer,
                    x, -y, z,
                    x, -y, -z,
                    x,  y, -z,
                    x,  y, z,
                    1, 0, 0,
                    light, overlay, color
            );

            quad(pose, buffer,
                    -x, -y, -z,
                    -x, -y, z,
                    -x,  y, z,
                    -x,  y, -z,
                    -1, 0, 0,
                    light, overlay, color
            );

            quad(pose, buffer,
                    -x, y, z,
                    x, y, z,
                    x, y, -z,
                    -x, y, -z,
                    0, 1, 0,
                    light, overlay, color
            );

            quad(pose, buffer,
                    -x, -y, -z,
                    x, -y, -z,
                    x, -y, z,
                    -x, -y, z,
                    0, -1, 0,
                    light,
                    overlay,
                    color);
        });
    }

    static void plane(PoseStack poseStack, SubmitNodeCollector collector, RenderType renderType, Direction.Axis axis, float width, float height, int light, int overlay, int color) {
        collector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
            float x = width / 2.0F;
            float y = height / 2.0F;

            quad(pose, buffer, -x, -y, 0, x, -y, 0, x,  y, 0, -x,  y, 0, 0, 0, 1, light, overlay, color);
        });
    }

    private static void quad(
            PoseStack.Pose pose,
            VertexConsumer buffer,
            float x0, float y0, float z0,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3,
            float nx, float ny, float nz,
            int light, int overlay, int color
    ) {
        VertexSupplier vertex = VertexSupplier.INSTANCE.reset().push(pose, buffer).color(color).light(light).overlay(overlay).normal(nx, ny, nz);

        vertex
                .uv(0, 1).emit(x0, y0, z0)
                .uv(1, 1).emit(x1, y1, z1)
                .uv(1, 0).emit(x2, y2, z2)
                .uv(0, 0).emit(x3, y3, z3);

        vertex.pop();
    }
}