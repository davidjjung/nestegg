package com.davigj.nest_egg.core.registry;

import com.davigj.nest_egg.core.NestEgg;
import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class NESoundEvents {
    public static final SoundSubRegistryHelper SOUNDS = NestEgg.REGISTRY_HELPER.getSoundSubHelper();

    public static final DeferredHolder<SoundEvent, SoundEvent> INCUBATOR_COO = SOUNDS.createSoundEvent("entity.bird.incubator_coo");
    public static final DeferredHolder<SoundEvent, SoundEvent> RACCOON_SNIFF = SOUNDS.createSoundEvent("entity.raccoon.sniff");
    public static final DeferredHolder<SoundEvent, SoundEvent> EGG_CRACK = SOUNDS.createSoundEvent("block.nest.crack");
    public static final DeferredHolder<SoundEvent, SoundEvent> EGG_HATCH = SOUNDS.createSoundEvent("block.nest.hatch");
}
