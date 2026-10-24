package dev.proststuff.utilitary.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix3x2fStack;

@Environment(EnvType.CLIENT)
public interface Scalable extends StackTransformationElement {
    float getScaleX();
    float getScaleY();

    default void pushScale(Matrix3x2fStack stack) {
        stack.scale(getScaleX(), getScaleY());
    }
}