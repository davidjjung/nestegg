package com.davigj.nest_egg.common.block.entity;

import com.davigj.nest_egg.common.block.EmuNestBlock;
import com.davigj.nest_egg.core.registry.NEBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.VanillaHopperItemHandler;

import javax.annotation.Nonnull;

public class EmuNestBlockEntity extends BlockEntity {
    public EmuNestBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType) NEBlockEntityTypes.EMU_NEST.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EmuNestBlockEntity blockEntity) {
        BlockPos blockpos = pos.below();
        EmuNestBlock block = (EmuNestBlock)state.getBlock();
        if (level.getBlockState(blockpos).hasBlockEntity()) {
            BlockEntity blockEntityBelow = level.getBlockEntity(blockpos);
            if (blockEntityBelow instanceof HopperBlockEntity && !((HopperBlockEntity)blockEntityBelow).isOnCooldown() && insertEggToHopper(blockEntityBelow, new ItemStack(block.getEgg()))) {
                level.setBlock(pos, block.getEmptyNest().defaultBlockState(), 2);
            }
        }

    }

    private static boolean insertEggToHopper(BlockEntity blockEntity, @Nonnull ItemStack stack) {
        HopperBlockEntity hopper = (HopperBlockEntity)blockEntity;
        VanillaHopperItemHandler inventory = new VanillaHopperItemHandler(hopper);
        if (!stack.isEmpty()) {
            stack = ItemHandlerHelper.insertItemStacked(inventory, stack, false);
        }

        if (!stack.isEmpty()) {
            return false;
        } else {
            if (!hopper.isOnCustomCooldown()) {
                hopper.setCooldown(8);
            }

            return true;
        }
    }
}
