package com.davigj.nest_egg.core.registry;

import com.davigj.nest_egg.core.NestEgg;
import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NESoundEvents {
    private static final SoundSubRegistryHelper HELPER = NestEgg.REGISTRY_HELPER.getSoundSubHelper();

    public static final RegistryObject<SoundEvent> INCUBATOR_COO = HELPER.createSoundEvent("entity.bird.incubator_coo");
    public static final RegistryObject<SoundEvent> RACCOON_SNIFF = HELPER.createSoundEvent("entity.raccoon.sniff");
    public static final RegistryObject<SoundEvent> EGG_CRACK = HELPER.createSoundEvent("block.nest.crack");
    public static final RegistryObject<SoundEvent> EGG_HATCH = HELPER.createSoundEvent("block.nest.hatch");
}
