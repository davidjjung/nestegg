package com.davigj.nest_egg.common.entity.ai.goal;

import com.davigj.nest_egg.common.block.EmuNestBlock;
import com.davigj.nest_egg.core.NEConfig;
import com.davigj.nest_egg.core.NestEgg;
import com.davigj.nest_egg.core.other.NECompatUtil;
import com.davigj.nest_egg.core.registry.NESoundEvents;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.blueprint.core.api.EggLayer;
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

public class EmuIncubateGoal extends MoveToBlockGoal {
    static TrackedDataManager manager = TrackedDataManager.INSTANCE;
    protected int ticksWaited;
    protected EggLayer eggLayer;

    public EmuIncubateGoal(EggLayer eggLayer, double speedIn) {
        super((Animal) eggLayer, speedIn, 16);
        this.eggLayer = eggLayer;
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos.above());
        Block block = blockstate.getBlock();
        return block instanceof EmuNestBlock nest && nest.getEgg() == eggLayer.getEggItem();
    }

    public void tick() {
        if (this.isReachedTarget()) {
            if (this.ticksWaited >= NEConfig.COMMON.emuIncubationTime.get()) {
                this.onReachedTarget();
            } else {
                if (ticksWaited > NEConfig.COMMON.emuIncubationTime.get() - 80) {
                    if (this.mob.getRandom().nextFloat() < 0.06F) {
                        ItemParticleOption shell = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(NECompatUtil.emuEgg));
                        ((ServerLevel) this.mob.level()).sendParticles(shell, this.blockPos.getX() + 0.5, this.blockPos.getY() + 1.4,
                                this.blockPos.getZ() + 0.5, 1, 0, 0.05D, 0, 0.08D);
                        this.mob.playSound(NESoundEvents.EGG_CRACK.get(), 1.0F, 1.0F);
                    }
                    this.mob.getLookControl().setLookAt(this.blockPos.getX() + 0.5, this.blockPos.getY(), this.blockPos.getZ() + 0.5);
                }
                if (this.mob.getRandom().nextFloat() < 0.006F) {
                    NECompatUtil.playEmuIncubateSound(this.mob);
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
                hatchEgg(state, level);
                manager.setValue(this.mob, NestEgg.EMU_INCUBATION_COOLDOWN, NEConfig.COMMON.emuIncubationCooldown.get());
                this.stop();
            }
        }
    }

    private void hatchEgg(BlockState state, Level level) {
        if (state.getBlock() instanceof EmuNestBlock nest) {
            // Audiovisual FX
            this.mob.playSound(NESoundEvents.EGG_HATCH.get());
            ItemParticleOption shell = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(NECompatUtil.emuEgg));
            for (int i = 0; i < 4; i++) {
                ((ServerLevel) this.mob.level()).sendParticles(shell, this.blockPos.getX() + 0.5, this.blockPos.getY() + 1.4,
                        this.blockPos.getZ() + 0.5, 1, 0, 0.05D, 0, 0.15D);
            }
            // Diminish nest a bit
                level.setBlock(this.blockPos.above(), nest.getEmptyNest().defaultBlockState(), 3);
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
        return !this.mob.isBaby() && manager.getValue(this.mob, NestEgg.EMU_INCUBATION_COOLDOWN) == 0 &&
                this.mob.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.blockPos.above())).size() < 2;
    }

    public void start() {
        super.start();
        this.ticksWaited = 0;
    }

    public double acceptedDistance() {
        return 0.8D;
    }
}
