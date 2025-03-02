package com.davigj.nest_egg.common.entity.ai.goal;

import com.davigj.nest_egg.core.NEConfig;
import com.davigj.nest_egg.core.NestEgg;
import com.davigj.nest_egg.core.registry.NESoundEvents;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.blueprint.core.api.EggLayer;
import com.teamabnormals.incubation.common.block.BirdNestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;

import static com.teamabnormals.incubation.common.block.BirdNestBlock.EGGS;

public class IncubateGoal extends MoveToBlockGoal {
    static TrackedDataManager manager = TrackedDataManager.INSTANCE;
    protected int ticksWaited;
    protected EggLayer eggLayer;

    public IncubateGoal(EggLayer eggLayer, double speedIn) {
        super((Animal) eggLayer, speedIn, 16);
        this.eggLayer = eggLayer;
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos.above());
        Block block = blockstate.getBlock();
        return block instanceof BirdNestBlock nest && nest.getEgg() == eggLayer.getEggItem();
    }

    public void tick() {
        if (this.isReachedTarget()) {
            if (this.ticksWaited >= NEConfig.COMMON.incubationTime.get()) {
                this.onReachedTarget();
            } else {
                if (ticksWaited > NEConfig.COMMON.incubationTime.get() - 100) {
                    if (!NEConfig.COMMON.ambient.get() && this.mob.getRandom().nextFloat() < 0.06F) {
                        ItemParticleOption shell = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(eggLayer.getEggItem()));
                        ((ServerLevel) this.mob.level()).sendParticles(shell, this.blockPos.getX() + 0.5, this.blockPos.getY() + 1.4,
                                this.blockPos.getZ() + 0.5, 1, 0, 0.05D, 0, 0.08D);
                        this.mob.playSound(NESoundEvents.EGG_CRACK.get(), 1.0F, 1.0F);
                    }
                    this.mob.getLookControl().setLookAt(this.blockPos.getX() + 0.5, this.blockPos.getY(), this.blockPos.getZ() + 0.5);
                }
                if (this.mob.getRandom().nextFloat() < 0.01F) {
                    this.mob.playSound(NESoundEvents.INCUBATOR_COO.get(), 1.0F, 1.0F);
                }
                ++this.ticksWaited;
            }
        }
        super.tick();
    }

    protected void onReachedTarget() {
        Level level = this.mob.level();
        if (this.mob.tickCount % 3 == 0) {
            if (ForgeEventFactory.getMobGriefingEvent(level, this.mob) && !level.isClientSide) {
                BlockState state = level.getBlockState(this.blockPos.above());
                if (!NEConfig.COMMON.ambient.get()) {
                    hatchEgg(state, level);
                }
                manager.setValue(this.mob, NestEgg.INCUBATION_COOLDOWN, NEConfig.COMMON.incubationCooldown.get());
                this.stop();
            }
        }
    }

    private void hatchEgg(BlockState state, Level level) {
        if (state.getBlock() instanceof BirdNestBlock nest) {
            // Audiovisual FX
            this.mob.playSound(NESoundEvents.EGG_HATCH.get());
            ItemParticleOption shell = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(eggLayer.getEggItem()));
            for (int i = 0; i < 4; i++) {
                ((ServerLevel) this.mob.level()).sendParticles(shell, this.blockPos.getX() + 0.5, this.blockPos.getY() + 1.4,
                        this.blockPos.getZ() + 0.5, 1, 0, 0.05D, 0, 0.15D);
            }
            // Diminish nest a bit
            int i = (Integer) state.getValue(EGGS);
            if (i > 1) {
                level.setBlock(this.blockPos.above(), (BlockState) state.setValue(EGGS, i - 1), 3);
            } else {
                level.setBlock(this.blockPos.above(), nest.getEmptyNest().defaultBlockState(), 3);
            }
            // Spawn chickadee chickadee
            Animal bird = (Animal) this.mob.getType().create(this.mob.level());
            if (bird != null) {
                bird.setAge(-24000);
                bird.moveTo(this.blockPos.getX() + 0.5, this.mob.getY(), this.blockPos.getZ() + 0.5, this.mob.getYRot(), 0.0F);
                this.mob.level().addFreshEntity(bird);
            }
        }
    }

    public boolean canUse() {
        return this.canIncubate() && super.canUse();
    }

    public boolean canContinueToUse() {
        return this.canIncubate() && super.canContinueToUse();
    }

    private boolean canIncubate() {
        return !this.mob.isBaby() && feedReqMet() && manager.getValue(this.mob, NestEgg.INCUBATION_COOLDOWN) == 0 &&
                this.mob.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.blockPos.above())).size() < 2;
    }

    private boolean feedReqMet() {
        return !NEConfig.COMMON.birdFeed.get() || manager.getValue(this.mob, NestEgg.FEED_TIMER) != 0;
    }

    public void start() {
        super.start();
        this.ticksWaited = 0;
    }

    public double acceptedDistance() {
        return 0.8D;
    }
}
