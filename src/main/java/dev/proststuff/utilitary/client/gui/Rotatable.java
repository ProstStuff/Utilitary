package dev.proststuff.utilitary.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix3x2fStack;

@Environment(EnvType.CLIENT)
public interface Rotatable extends StackTransformationElement {
    float getRotation();

    default void pushRotation(Matrix3x2fStack stack) {
        stack.rotate((float) Math.toRadians(getRotation()));
    }
}
