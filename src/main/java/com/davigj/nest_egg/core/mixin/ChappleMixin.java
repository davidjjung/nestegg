package com.davigj.nest_egg.core.mixin;

import com.ninni.etcetera.entity.ChappleEntity;
import com.teamabnormals.blueprint.core.api.EggLayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

@Pseudo
@Mixin(ChappleEntity.class)
public abstract class ChappleMixin extends Chicken implements EggLayer {
    @Shadow public abstract ChappleEntity.Type getChappleType();

    @Shadow public abstract int getAppleLayTime();

    @Shadow public abstract void setAppleLayTime(int appleLayTime);

    public ChappleMixin(EntityType<? extends Chicken> p_28236_, Level p_28237_) {
        super(p_28236_, p_28237_);
    }

    @Override
    public int getEggTimer() {
        return this.getAppleLayTime();
    }

    @Override
    public void setEggTimer(int time) {
        this.setAppleLayTime(time);
    }

    @Override
    public boolean isBirdJockey() {
        return this.isChickenJockey();
    }

    @Override
    public Item getEggItem() {
        if (this.getChappleType() == ChappleEntity.Type.GOLDEN && this.random.nextInt(3) == 0) {
            return Items.GOLDEN_APPLE;
        } else {
            return Items.APPLE;
        }
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
