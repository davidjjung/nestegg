package com.davigj.nest_egg.core.other.compat;

import com.ninni.spawn.registry.SpawnItems;
import com.ninni.spawn.server.entity.mob.reptile.Booby;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import static com.davigj.nest_egg.core.other.compat.ModConstants.SPAWN;

public class SpawnCompat {
    public static final Item boobyEgg;

    public static boolean isBooby(Entity entity) {
        return entity instanceof Booby;
    }

    static {
        boobyEgg = SPAWN ? SpawnItems.BOOBY_EGG.get().asItem() : Items.AIR;
    }
}
