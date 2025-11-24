package dev.proststuff.utilitary.block.state;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface Illuminance {
    IntProperty ILLUMINANCE = IntProperty.of("illuminance", 0, 15);

    default int getDefaultIlluminance() {
        return 0;
    }

    default void forceIlluminanceUpdate(BlockState state, World world, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.offset(dir);
            BlockState neighborState = world.getBlockState(neighborPos);

            BlockState updated = state.getStateForNeighborUpdate(dir, neighborState, world, pos, neighborPos);
            if (updated != state) {
                world.setBlockState(pos, updated, Block.NOTIFY_LISTENERS);
            }
        }
    }

    default BlockState updateIlluminance(BlockState state, int illuminance) {
        return state.with(ILLUMINANCE, illuminance);
    }

    static int getIlluminance(BlockState state) {
        return state.get(ILLUMINANCE);
    }
}