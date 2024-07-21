package com.davigj.nest_egg.core.mixin;

import com.davigj.nest_egg.core.NEConfig;
import com.davigj.nest_egg.core.other.NECompatUtil;
import com.teamabnormals.blueprint.core.api.EggLayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.blackgear.geologicexpansion.common.entity.duck.Duck")
public abstract class GEDuckMixin extends Animal implements EggLayer {
    protected GEDuckMixin(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    @Shadow
    private int eggTime;

    @Override
    public int getEggTimer() {
        return this.eggTime;
    }

    @Override
    public void setEggTimer(int time) {
        this.eggTime = time;
    }

    @Override
    public boolean isBirdJockey() {
        return false;
    }

    @Override
    public Item getEggItem() {
        return NECompatUtil.duckEgg;
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