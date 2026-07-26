package dev.proststuff.utilitary.api.v1.client.gui;

import net.minecraft.client.gui.layouts.LayoutElement;
import org.joml.Matrix3x2fStack;

public interface StackTransformationElement extends LayoutElement {
    default void push(Matrix3x2fStack stack) {
        stack.pushMatrix();

        float centerX = getX() + getWidth() / 2.0F;
        float centerY = getY() + getHeight() / 2.0F;

        stack.translate(centerX, centerY);
        transformFromCenter(stack);
        stack.translate(-centerX, -centerY);
    }

    default void pop(Matrix3x2fStack stack) {
        stack.popMatrix();
    }

    default void transformFromCenter(Matrix3x2fStack stack) {
        if (this instanceof Rotatable rotatable) rotatable.pushRotation(stack);
        if (this instanceof Scalable scalable) scalable.pushScale(stack);
    }
}
