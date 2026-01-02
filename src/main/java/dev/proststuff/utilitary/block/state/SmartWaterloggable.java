package dev.proststuff.utilitary.block.state;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public interface SmartWaterloggable extends Waterloggable {
    BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    default BlockState withWaterlogged(BlockState state, ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return state.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    default FluidState findFluidState(BlockState state) {
        return state.get(WATERLOGGED)
                ? Fluids.WATER.getStill(false)
                : Fluids.EMPTY.getDefaultState();
    }

    @Override
    default boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.get(WATERLOGGED) && fluidState.getFluid() == Fluids.WATER) {
            if (!world.isClient()) {
                world.setBlockState(pos, state.with(WATERLOGGED, true), Block.NOTIFY_ALL);
                world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }
            return true;
        }
        return false;
    }

    default void updateWaterloggedState(BlockState state, WorldAccess world, BlockPos pos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
    }

    default void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState) {
        if (state.get(WATERLOGGED) && !state.isOf(newState.getBlock())) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
    }
}