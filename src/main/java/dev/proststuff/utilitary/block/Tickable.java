package dev.proststuff.utilitary.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Tickable {
    static void ticker(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (!world.isClient && blockEntity instanceof Tickable tickable) {
            int ticks = tickable.getTicks();
            int interval = tickable.getMaxInterval(world, pos, state);

            if (interval > 0) {
                tickable.tick(world, pos, state);

                if (tickable.getTicks() >= interval) {
                    tickable.onIntervalTick(world, pos, state);
                    tickable.resetTick();
                }
            }
        }
    }

    int getMaxInterval(World world, BlockPos pos, BlockState state);
    int getTicks();
    void resetTick();

    void tick(World world, BlockPos pos, BlockState state);
    void onIntervalTick(World world, BlockPos pos, BlockState state);
}