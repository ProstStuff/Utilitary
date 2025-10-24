package dev.proststuff.utilitary.utility;

import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.EnumMap;

public class DirectionalVoxelShape {
    private final EnumMap<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);

    public DirectionalVoxelShape(VoxelShape base) {
        shapes.put(Direction.NORTH, base);
        shapes.put(Direction.EAST, rotateYaw(base, 1));
        shapes.put(Direction.SOUTH, rotateYaw(base, 2));
        shapes.put(Direction.WEST, rotateYaw(base, 3));
        shapes.put(Direction.UP, rotatePitch(base, -1));
        shapes.put(Direction.DOWN, rotatePitch(base, 1));
    }

    public VoxelShape get(Direction dir) {
        return shapes.getOrDefault(dir, shapes.get(Direction.NORTH));
    }

    private static VoxelShape rotateYaw(VoxelShape shape, int turns) {
        VoxelShape current = shape;
        for (int i = 0; i < turns; i++) {
            VoxelShape rotated = VoxelShapes.empty();
            for (var box : current.getBoundingBoxes()) {
                double minX = box.minX * 16;
                double minY = box.minY * 16;
                double minZ = box.minZ * 16;
                double maxX = box.maxX * 16;
                double maxY = box.maxY * 16;
                double maxZ = box.maxZ * 16;

                rotated = VoxelShapes.union(rotated,
                        Block.createCuboidShape(
                                Math.round(16 - maxZ), Math.round(minY), Math.round(minX),
                                Math.round(16 - minZ), Math.round(maxY), Math.round(maxX)
                        ));
            }
            current = rotated;
        }
        return current.simplify();
    }

    private static VoxelShape rotatePitch(VoxelShape shape, int turns) {
        VoxelShape current = shape;
        int steps = Math.abs(turns);
        for (int i = 0; i < steps; i++) {
            VoxelShape rotated = VoxelShapes.empty();
            for (var box : current.getBoundingBoxes()) {
                double minX = box.minX * 16;
                double minY = box.minY * 16;
                double minZ = box.minZ * 16;
                double maxX = box.maxX * 16;
                double maxY = box.maxY * 16;
                double maxZ = box.maxZ * 16;

                if (turns > 0) {
                    rotated = VoxelShapes.union(rotated,
                            Block.createCuboidShape(
                                    Math.round(minX), Math.round(16 - maxZ), Math.round(minY),
                                    Math.round(maxX), Math.round(16 - minZ), Math.round(maxY)
                            ));
                } else {
                    rotated = VoxelShapes.union(rotated,
                            Block.createCuboidShape(
                                    Math.round(minX), Math.round(minZ), Math.round(16 - maxY),
                                    Math.round(maxX), Math.round(maxZ), Math.round(16 - minY)
                            ));
                }
            }
            current = rotated;
        }
        return current.simplify();
    }
}