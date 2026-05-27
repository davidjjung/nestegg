package com.davigj.nest_egg.core.other.compat;

import net.minecraft.world.entity.Entity;

public class EnvironmentalCompat {
    public static boolean isEnvironmentalDuck(Entity entity) {
        return entity instanceof com.teamabnormals.environmental.common.entity.animal.Duck;
    }
}
