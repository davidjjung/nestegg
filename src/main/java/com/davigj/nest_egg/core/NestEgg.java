package com.davigj.nest_egg.core;

import com.davigj.nest_egg.core.registry.NEBlocks;
import com.davigj.nest_egg.core.registry.NESoundEvents;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.incubation.core.Incubation;
import com.teamabnormals.incubation.core.registry.IncubationItems;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(NestEgg.MOD_ID)
public class NestEgg {
    public static final String MOD_ID = "nest_egg";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

    public static final TrackedData<Integer> THIEF_TIMER = TrackedData.Builder.create(ByteBufCodecs.INT, () -> 0).enablePersistence().build();
    public static final TrackedData<Integer> INCUBATION_COOLDOWN = TrackedData.Builder.create(ByteBufCodecs.INT, () -> 0).enablePersistence().build();
    public static final TrackedData<Integer> EMU_INCUBATION_COOLDOWN = TrackedData.Builder.create(ByteBufCodecs.INT, () -> 0).enablePersistence().build();
    public static final TrackedData<Integer> FEED_TIMER = TrackedData.Builder.create(ByteBufCodecs.INT, () -> 0).enablePersistence().build();
    public static final TrackedData<Integer> FLEE_TIMER = TrackedData.Builder.create(ByteBufCodecs.INT, () -> 0).enablePersistence().build();

    public NestEgg(IEventBus bus, ModContainer container) {
        NEBlocks.BLOCKS.register(bus);
        NEBlocks.ITEMS.register(bus);
        NESoundEvents.SOUNDS.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::dataSetup);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            NEBlocks.setupTabEditors();
        }

        container.registerConfig(ModConfig.Type.COMMON, NEConfig.COMMON_SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            TrackedDataManager.INSTANCE.registerData(NestEgg.location("thief_timer"), THIEF_TIMER);
            TrackedDataManager.INSTANCE.registerData(NestEgg.location("incubation_cooldown"), INCUBATION_COOLDOWN);
            TrackedDataManager.INSTANCE.registerData(NestEgg.location("emu_incubation_cooldown"), EMU_INCUBATION_COOLDOWN);
            TrackedDataManager.INSTANCE.registerData(NestEgg.location("feed_timer"), FEED_TIMER);
            TrackedDataManager.INSTANCE.registerData(NestEgg.location("flee_timer"), FLEE_TIMER);
        });
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    private void dataSetup(GatherDataEvent event) {

    }

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}