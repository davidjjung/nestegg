package com.davigj.nest_egg.core.other.compat;

import com.davigj.nest_egg.core.NEConfig;
import com.github.alexthe666.alexsmobs.entity.EntityCrow;
import com.github.alexthe666.alexsmobs.entity.EntityEmu;
import com.github.alexthe666.alexsmobs.entity.EntityRaccoon;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.fml.ModList;

import static com.davigj.nest_egg.core.other.compat.ModConstants.ALEXSMOBS;

public class AMCompat {
    public static final Item emuEgg;
    public static boolean isEmu(Entity entity) {
        return entity instanceof EntityEmu;
    }

    public static boolean isRaccoon(Entity entity) {
        return entity instanceof EntityRaccoon;
    }

    public static boolean isCrow(Entity entity) {
        return entity instanceof EntityCrow;
    }

    public static boolean isCrowFlying(Entity entity) {
        if (entity instanceof EntityCrow crow) {
            return crow.isFlying();
        } else {
            return false;
        }
    }

    public static void setNotFlying(Entity entity) {
        if (entity instanceof EntityCrow crow) {
            if (!crow.isFlying() || !crow.isBaby()) {
                crow.setFlying(false);
            }
        }
    }

    public static void playEmuIncubateSound(PathfinderMob mob) {
        if (ModList.get().isLoaded("alexsmobs") && (isEmu(mob))) {
            mob.playSound(AMSoundRegistry.EMU_IDLE.get(), 1.0F, 0.7F);
        }
    }

    public static void angryEmus(Level level, BlockPos pos, LivingEntity target) {
        if (!NEConfig.COMMON.emuAggro.get()) return;
        int mercy = 3;
        for (EntityEmu emu : level.getEntitiesOfClass(EntityEmu.class, new AABB(pos).inflate(10))) {
            if (!emu.isBaby() && mercy > 0) {
                emu.playSound((SoundEvent) AMSoundRegistry.EMU_HURT.get(), 1.0F, emu.getVoicePitch());
                emu.setTarget(target);
                mercy--;
            }
        }
    }

    static {
        emuEgg = ALEXSMOBS ? AMItemRegistry.EMU_EGG.get() : Items.AIR;
    }
}
