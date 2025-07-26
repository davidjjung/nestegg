package com.davigj.nest_egg.core.other;

import com.blackgear.geologicexpansion.common.registries.GEItems;
import com.davigj.nest_egg.core.NEConfig;
import com.github.alexthe666.alexsmobs.entity.EntityCrow;
import com.github.alexthe666.alexsmobs.entity.EntityEmu;
import com.github.alexthe666.alexsmobs.entity.EntityRaccoon;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.starfish_studios.naturalist.common.entity.Duck;
import com.starfish_studios.naturalist.core.registry.NaturalistRegistry;
import com.teamabnormals.autumnity.common.entity.animal.Turkey;
import com.teamabnormals.autumnity.core.registry.AutumnityItems;
import com.teamabnormals.autumnity.core.registry.AutumnitySoundEvents;
import com.teamabnormals.incubation.common.block.BirdNestBlock;
import net.dylanvhs.bountiful_critters.entity.custom.land.EmuEntity;
import net.dylanvhs.bountiful_critters.entity.custom.land.PheasantEntity;
import net.dylanvhs.bountiful_critters.item.ModItems;
import net.dylanvhs.bountiful_critters.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.ModList;

public class NECompatUtil {
    public static final Item emuEgg;
    public static final Item BEemuEgg;
    public static final Item pheasantEgg;
    public static final Item naturalistDuckEgg;
    public static final Item GEduckEgg;

    public static void angryTurkeys(BirdNestBlock nest, Level level, BlockPos pos, LivingEntity target) {
        if (!ModList.get().isLoaded("autumnity") || !NEConfig.COMMON.turkeyAggro.get()) return;
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

    public static boolean isEnvironmentalDuck(Entity entity) {
        return entity instanceof com.teamabnormals.environmental.common.entity.animal.Duck;
    }

    public static boolean isNaturalistDuck(Entity entity) {
        return entity instanceof Duck;
    }

    public static boolean isGEDuck(Entity entity) {
        return entity instanceof com.blackgear.geologicexpansion.common.entity.duck.Duck;
    }

    public static boolean isRaccoon(Entity entity) {
        return entity instanceof EntityRaccoon;
    }

    public static boolean isEmu(Entity entity) {
        return entity instanceof EntityEmu;
    }

    public static boolean isBCEmu(Entity entity) {
        return entity instanceof EmuEntity;
    }

    public static boolean isPheasant(Entity entity) {
        return entity instanceof PheasantEntity;
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
        } else if (ModList.get().isLoaded("bountiful_critters") && isBCEmu(mob)) {
            mob.playSound(ModSounds.EMU_AMBIENT.get(), 1.0F, 0.7F);
        }
    }

    static {
        emuEgg = ModList.get().isLoaded("alexsmobs") ? AMItemRegistry.EMU_EGG.get() : Items.AIR;
        BEemuEgg = ModList.get().isLoaded("bountiful_critters") ? ModItems.EMU_EGG.get() : Items.AIR;
        pheasantEgg = ModList.get().isLoaded("bountiful_critters") ? ModItems.PHEASANT_EGG.get() : Items.AIR;
        naturalistDuckEgg = ModList.get().isLoaded("naturalist") ? NaturalistRegistry.DUCK_EGG.get() : Items.AIR;
        GEduckEgg = ModList.get().isLoaded("geologicexpansion") ? GEItems.DUCK_EGG.get() : Items.AIR;
    }
}
