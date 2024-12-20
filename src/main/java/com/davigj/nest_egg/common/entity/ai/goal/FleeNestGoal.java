/* package com.davigj.nest_egg.common.entity.ai.goal;

import com.davigj.nest_egg.core.NestEgg;
import com.github.alexthe666.alexsmobs.entity.EntityCrow;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FleeNestGoal extends Goal {
    // Re-implemented AM Shoebill code. It works alright, but turkeys are still able to track them the entire flightpath
    // so unimplemented for now lol

    static TrackedDataManager manager = TrackedDataManager.INSTANCE;
    private final EntityCrow thief;
    private BlockPos currentTarget = null;
    private int executionTime = 0;

    public FleeNestGoal(EntityCrow thief) {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.thief = thief;
    }

    public void stop() {
        this.currentTarget = null;
        this.executionTime = 0;
        this.thief.setFlying(false);
    }

    public boolean canContinueToUse() {
        return this.thief.isFlying() && (this.executionTime < 15 || !this.thief.onGround());
    }

    public boolean canUse() {
        return manager.getValue(this.thief, NestEgg.FLEE_TIMER) > 0 && this.thief.onGround();
    }

    public void start() {
        if (this.thief.onGround()) {
            this.thief.setFlying(true);
        }

    }

    public void tick() {
        ++this.executionTime;
        if (this.currentTarget == null) {
            if (manager.getValue(this.thief, NestEgg.FLEE_TIMER) == 0) {
                this.currentTarget = this.getBlockGrounding(this.thief.position());
            } else {
                this.currentTarget = this.getBlockInViewAway(this.thief.position());
            }
        }

        if (this.currentTarget != null) {
            this.thief.getNavigation().moveTo((double)((float)this.currentTarget.getX() + 0.5F), (double)((float)this.currentTarget.getY() + 0.5F), (double)((float)this.currentTarget.getZ() + 0.5F), 1.5);
            if (this.thief.distanceToSqr(Vec3.atCenterOf(this.currentTarget)) < 4.0) {
                this.currentTarget = null;
            }
        }

        if (manager.getValue(this.thief, NestEgg.FLEE_TIMER) == 0 && (this.thief.isInWater() || !this.thief.level().isEmptyBlock(this.thief.blockPosition().below()))) {
            this.stop();
            this.thief.setFlying(false);
        }

    }

    public BlockPos getBlockInViewAway(Vec3 fleePos) {
        float radius = -9.45F - (float)this.thief.getRandom().nextInt(48);
        float neg = this.thief.getRandom().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.thief.yBodyRot;
        float angle = 0.017453292F * renderYawOffset + 3.15F + this.thief.getRandom().nextFloat() * neg;
        double extraX = (double)(radius * Mth.sin(3.1415927F + angle));
        double extraZ = (double)(radius * Mth.cos(angle));
        BlockPos radialPos = AMBlockPos.fromCoords(fleePos.x() + extraX, 0.0, fleePos.z() + extraZ);
        BlockPos ground = this.thief.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, radialPos);
        int distFromGround = (int)this.thief.getY() - ground.getY();
        int flightHeight = 4 + this.thief.getRandom().nextInt(10);
        BlockPos newPos = radialPos.above(distFromGround > 8 ? flightHeight : (int)this.thief.getY() + this.thief.getRandom().nextInt(6) + 1);
        return !this.thief.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.thief.distanceToSqr(Vec3.atCenterOf(newPos)) > 6.0 ? newPos : null;
    }

    public BlockPos getBlockGrounding(Vec3 fleePos) {
        float radius = -9.45F - (float)this.thief.getRandom().nextInt(24);
        float neg = this.thief.getRandom().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.thief.yBodyRot;
        float angle = 0.017453292F * renderYawOffset + 3.15F + this.thief.getRandom().nextFloat() * neg;
        double extraX = (double)(radius * Mth.sin(3.1415927F + angle));
        double extraZ = (double)(radius * Mth.cos(angle));
        BlockPos radialPos = AMBlockPos.fromCoords(fleePos.x() + extraX, 0.0, fleePos.z() + extraZ);
        BlockPos ground = this.thief.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, radialPos);
        return !this.thief.isTargetBlocked(Vec3.atCenterOf(ground.above())) ? ground : null;
    }
} */
