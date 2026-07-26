package dev.proststuff.utilitary.api.v1.client.gui;

import org.joml.Matrix3x2fStack;

public interface Scalable extends StackTransformationElement {
    float getScaleX();
    float getScaleY();

    default void pushScale(Matrix3x2fStack stack) {
        stack.scale(getScaleX(), getScaleY());
    }
}