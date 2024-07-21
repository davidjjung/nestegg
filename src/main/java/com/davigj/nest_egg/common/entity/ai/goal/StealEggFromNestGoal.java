package com.davigj.nest_egg.common.entity.ai.goal;

import com.davigj.nest_egg.core.NEConfig;
import com.davigj.nest_egg.core.NestEgg;
import com.davigj.nest_egg.core.other.NECompatUtil;
import com.davigj.nest_egg.core.registry.NESoundEvents;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.incubation.common.block.BirdNestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

import static com.teamabnormals.incubation.common.block.BirdNestBlock.EGGS;

public class StealEggFromNestGoal extends MoveToBlockGoal {
    static TrackedDataManager manager = TrackedDataManager.INSTANCE;
    private final Animal thief;
    protected int ticksWaited;

    public StealEggFromNestGoal(Animal animal, double speedIn) {
        super(animal, speedIn, 16);
        this.thief = animal;
    }

    public boolean requiresUpdateEveryTick() {
        return this.ticksWaited > 70;
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos.above());
        Block block = blockstate.getBlock();
        return block instanceof BirdNestBlock;
    }

    public void tick() {
        if (this.isReachedTarget()) {
            if (this.ticksWaited >= 70) {
                this.onReachedTarget();
            } else {
                if (this.ticksWaited > 15) {
                    if (this.ticksWaited < 25) {
                        this.mob.getLookControl().setLookAt(this.blockPos.getX() + 1.25, this.mob.getEyeY(), this.blockPos.getZ() + 1.25);
                    } else if (this.ticksWaited < 40) {
                        this.mob.getLookControl().setLookAt(this.blockPos.getX() - 0.25, this.mob.getEyeY(), this.blockPos.getZ() - 0.25);
                    } else {
                        this.mob.getLookControl().setLookAt(this.blockPos.getX() + 0.5, this.blockPos.getY(), this.blockPos.getZ() + 0.5);
                    }
                }
                if (this.thief.getRandom().nextFloat() < 0.065F) {
                    this.thief.playSound(NESoundEvents.RACCOON_SNIFF.get(), 1.2F, 1.0F);
                }
                ++this.ticksWaited;
            }
        } else if (!this.isReachedTarget() && this.thief.getRandom().nextFloat() < 0.05F) {
            this.thief.playSound(NESoundEvents.RACCOON_SNIFF.get(), 1.0F, 0.7F);
        }

        super.tick();
    }

    protected void onReachedTarget() {
        Level level = this.thief.level();
        if (ForgeEventFactory.getMobGriefingEvent(level, this.thief) && !level.isClientSide) {
            BlockState state = level.getBlockState(this.blockPos.above());
            if (state.getBlock() instanceof BirdNestBlock nest) {
                Block.popResource(level, this.blockPos.above(), new ItemStack((ItemLike)nest.getEgg()));
                int i = (Integer)state.getValue(EGGS);
                if (i > 1) {
                    level.setBlock(this.blockPos.above(), (BlockState)state.setValue(EGGS, i - 1), 3);
                } else {
                    level.setBlock(this.blockPos.above(), nest.getEmptyNest().defaultBlockState(), 3);
                }
                this.thief.playSound(SoundEvents.ITEM_PICKUP);
                NECompatUtil.angryTurkeys(nest, level, blockPos.above(), this.thief);
                manager.setValue(this.thief, NestEgg.THIEF_TIMER, NEConfig.COMMON.thiefTimer.get()
                        + this.thief.getRandom().nextInt(NEConfig.COMMON.thiefTimerBonus.get()));
                manager.setValue(this.thief, NestEgg.FLEE_TIMER, 100 + this.thief.getRandom().nextInt(150));
            }
        }
    }

    public boolean canUse() {
        return this.tomfooleryAllowed() && super.canUse();
    }

    public boolean canContinueToUse() {
        return this.tomfooleryAllowed() && super.canContinueToUse();
    }

    private boolean tomfooleryAllowed() {
        return !((TamableAnimal)this.thief).isTame() && this.thief.getMainHandItem().isEmpty()
                && !this.thief.isBaby() && manager.getValue(this.thief, NestEgg.THIEF_TIMER) == 0;
    }

    public void start() {
        this.ticksWaited = 0;
        super.start();
    }
}
