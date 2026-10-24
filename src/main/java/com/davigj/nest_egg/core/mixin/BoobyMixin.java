package com.davigj.nest_egg.core.mixin;

import com.davigj.nest_egg.core.NestEgg;
import com.davigj.nest_egg.core.other.compat.NaturalistCompat;
import com.davigj.nest_egg.core.other.compat.SpawnCompat;
import com.ninni.spawn.server.entity.base.FlyingWalkingAnimal;
import com.ninni.spawn.server.entity.mob.reptile.Booby;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.blueprint.core.api.EggLayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(Booby.class)
public abstract class BoobyMixin extends FlyingWalkingAnimal implements EggLayer {
    private static TrackedDataManager manager = TrackedDataManager.INSTANCE;
    protected BoobyMixin(EntityType<? extends FlyingWalkingAnimal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    @Override
    public int getEggTimer() {
//        return manager.getValue(this, NestEgg.BOOBY_EGG_TIMER);
        return 100;
    }

    @Override
    public void setEggTimer(int time) {
//        manager.setValue(this, NestEgg.BOOBY_EGG_TIMER, time);
    }

    @Override
    public boolean isBirdJockey() {
        return false;
    }

    @Override
    public Item getEggItem() {
        return SpawnCompat.boobyEgg;
    }

    @Override
    public int getNextEggTime(RandomSource rand) {
        return rand.nextInt(6000) + 6000;
    }

    @Override
    public SoundEvent getEggLayingSound() {
        return SoundEvents.CHICKEN_EGG;
    }
}
