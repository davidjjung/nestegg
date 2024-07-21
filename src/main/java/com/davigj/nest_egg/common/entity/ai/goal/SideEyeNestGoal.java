package com.davigj.nest_egg.common.entity.ai.goal;

import com.teamabnormals.incubation.common.block.BirdNestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SideEyeNestGoal extends MoveToBlockGoal {
    private final Animal watcher;
    protected int ticksWaited;
    protected boolean bored;

    public SideEyeNestGoal(Animal animal) {
        super(animal, 1.0, 16);
        this.watcher = animal;
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(new BlockPos(pos.getX(), pos.above().getY(), pos.getZ() + 1));
        Block block = blockstate.getBlock();
        return block instanceof BirdNestBlock;
    }

    public void tick() {
        if (this.isReachedTarget()) {
            if (this.ticksWaited >= 200) {
                this.bored = true;
            } else {
                if (this.ticksWaited > 150) {
                    this.mob.getLookControl().setLookAt(this.blockPos.getX() + 0.5, this.watcher.getY(), this.blockPos.getZ() + 0.5);
                }
                ++this.ticksWaited;
            }
        }
        super.tick();
    }

    public boolean canUse() {
        return !this.bored && this.watcher.getMainHandItem().isEmpty() && super.canUse();
    }

    public boolean canContinueToUse() {
        return !this.bored && this.watcher.getMainHandItem().isEmpty() && super.canContinueToUse();
    }

    public void start() {
        this.ticksWaited = 0;
        super.start();
    }

    public double acceptedDistance() {
        return 2.0D;
    }
}
