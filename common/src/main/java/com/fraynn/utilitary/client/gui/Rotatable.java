package com.fraynn.utilitary.client.gui;

import org.joml.Matrix3x2fStack;

public interface Rotatable extends StackTransformationElement {
    float getRotation();

    default void pushRotation(Matrix3x2fStack stack) {
        stack.rotate((float) Math.toRadians(getRotation()));
    }
}
