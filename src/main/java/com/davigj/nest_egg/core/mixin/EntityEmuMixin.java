package com.davigj.nest_egg.core.mixin;

import com.davigj.nest_egg.core.other.compat.AMCompat;
import com.davigj.nest_egg.core.other.compat.NaturalistCompat;
import com.github.alexthe666.alexsmobs.entity.EntityEmu;
import com.teamabnormals.blueprint.core.api.EggLayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

@Pseudo
@Mixin(EntityEmu.class)
public abstract class EntityEmuMixin extends Animal implements EggLayer {
    @Shadow
    public int timeUntilNextEgg;

    protected EntityEmuMixin(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    @Override
    public int getEggTimer() {
        return this.timeUntilNextEgg;
    }

    @Override
    public void setEggTimer(int time) {
        this.timeUntilNextEgg = time;
    }

    @Override
    public boolean isBirdJockey() {
        return false;
    }

    @Override
    public Item getEggItem() {
        return AMCompat.emuEgg;
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
