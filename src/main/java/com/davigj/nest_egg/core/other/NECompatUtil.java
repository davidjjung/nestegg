package com.davigj.nest_egg.core.other;

import com.blackgear.geologicexpansion.common.registries.GEItems;
import com.davigj.nest_egg.core.NEConfig;
import com.davigj.nest_egg.core.NestEgg;
import com.github.alexthe666.alexsmobs.entity.EntityEmu;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistItems;
import com.teamabnormals.autumnity.common.entity.animal.Turkey;
import com.teamabnormals.autumnity.core.registry.AutumnityEntityTypes;
import com.teamabnormals.autumnity.core.registry.AutumnityItems;
import com.teamabnormals.autumnity.core.registry.AutumnitySoundEvents;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.blueprint.core.api.EggLayer;
import com.teamabnormals.incubation.common.block.BirdNestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

public class NECompatUtil {
    public static final Item emuEgg;
    public static final Item duckEgg;
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

    static {
        emuEgg = ModList.get().isLoaded("alexsmobs") ? AMItemRegistry.EMU_EGG.get() : Items.EGG;
        duckEgg = ModList.get().isLoaded("naturalist") ? NaturalistItems.DUCK_EGG.get() : Items.EGG;
        GEduckEgg = ModList.get().isLoaded("geologicexpansion") ? GEItems.DUCK_EGG.get() : Items.EGG;
    }
}
