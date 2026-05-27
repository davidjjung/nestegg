package com.davigj.nest_egg.core.other.compat;

import com.davigj.nest_egg.core.NEConfig;
import com.teamabnormals.autumnity.common.entity.animal.Turkey;
import com.teamabnormals.autumnity.core.registry.AutumnityItems;
import com.teamabnormals.autumnity.core.registry.AutumnitySoundEvents;
import com.teamabnormals.incubation.common.block.BirdNestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class AutumnityCompat {
    public static void angryTurkeys(BirdNestBlock nest, Level level, BlockPos pos, LivingEntity target) {
        if (!NEConfig.COMMON.turkeyAggro.get()) return;
        if (!(nest.getEgg() == AutumnityItems.TURKEY_EGG.get())) return;
        if (target instanceof Player player && player.isCreative()) return;
        int mercy = 2;
        for (Turkey turkey : level.getEntitiesOfClass(Turkey.class, new AABB(pos).inflate(8))) {
            if (!turkey.isBaby() && mercy > 0) {
                turkey.playSound((SoundEvent) AutumnitySoundEvents.ENTITY_TURKEY_AGGRO.get(), 1.0F, turkey.getVoicePitch());
                turkey.setTarget(target);
                mercy--;
            }
        }
    }

    public static boolean isTurkey(Entity entity) {
        return entity instanceof Turkey;
    }
}
