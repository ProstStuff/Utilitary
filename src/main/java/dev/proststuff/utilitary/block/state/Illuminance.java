package dev.proststuff.utilitary.block.state;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Illuminance {
    IntProperty ILLUMINANCE = IntProperty.of("illuminance", 0, 15);

    default BlockState withIlluminance(BlockState state, ItemPlacementContext ctx) {
        return state.with(ILLUMINANCE, getDefaultIlluminance());
    }

    default int getDefaultIlluminance() {
        return 0;
    }

    default void setIlluminance(World world, BlockPos pos, BlockState state, int illuminance) {
        world.setBlockState(
                pos,
                state.with(Illuminance.ILLUMINANCE, illuminance),
                Block.NOTIFY_NEIGHBORS
        );
    }

    static int getIlluminance(BlockState state) {
        return state.get(ILLUMINANCE);
    }
}