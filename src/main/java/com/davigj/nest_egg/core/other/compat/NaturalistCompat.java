package com.davigj.nest_egg.core.other.compat;

import com.starfish_studios.naturalist.registry.NaturalistRegistry;
import com.starfish_studios.naturalist.server.entity.mob.Duck;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import static com.davigj.nest_egg.core.other.compat.ModConstants.NATURALIST;

public class NaturalistCompat {
    public static final Item naturalistDuckEgg;

    public static boolean isNaturalistDuck(Entity entity) {
        return entity instanceof Duck;
    }

    static {
        naturalistDuckEgg = NATURALIST ? NaturalistRegistry.DUCK_EGG.get() : Items.AIR;
    }
}
