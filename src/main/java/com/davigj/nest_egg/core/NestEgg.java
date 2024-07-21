package com.davigj.nest_egg.core;

import com.davigj.nest_egg.core.registry.NEBlocks;
import com.davigj.nest_egg.core.registry.NEItems;
import com.teamabnormals.blueprint.common.world.storage.tracking.DataProcessors;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NestEgg.MOD_ID)
public class NestEgg {
    public static final String MOD_ID = "nest_egg";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

    public static final TrackedData<Integer> THIEF_TIMER = TrackedData.Builder.create(DataProcessors.INT, () -> 0).enableSaving().enablePersistence().build();
    public static final TrackedData<Integer> INCUBATION_COOLDOWN = TrackedData.Builder.create(DataProcessors.INT, () -> 0).enableSaving().enablePersistence().build();
    public static final TrackedData<Integer> EMU_INCUBATION_COOLDOWN = TrackedData.Builder.create(DataProcessors.INT, () -> 0).enableSaving().enablePersistence().build();
    public static final TrackedData<Integer> FEED_TIMER = TrackedData.Builder.create(DataProcessors.INT, () -> 0).enableSaving().enablePersistence().build();
    public static final TrackedData<Integer> FLEE_TIMER = TrackedData.Builder.create(DataProcessors.INT, () -> 0).enableSaving().enablePersistence().build();

    public NestEgg() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext context = ModLoadingContext.get();
        MinecraftForge.EVENT_BUS.register(this);

		REGISTRY_HELPER.register(bus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            NEBlocks.buildCreativeTabContents();
        });

        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::dataSetup);
        context.registerConfig(ModConfig.Type.COMMON, NEConfig.COMMON_SPEC);

        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(MOD_ID, "thief_timer"), THIEF_TIMER);
        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(MOD_ID, "incubation_cooldown"), INCUBATION_COOLDOWN);
        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(MOD_ID, "emu_incubation_cooldown"), EMU_INCUBATION_COOLDOWN);
        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(MOD_ID, "feed_timer"), FEED_TIMER);
        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(MOD_ID, "flee_timer"), FLEE_TIMER);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    private void dataSetup(GatherDataEvent event) {

    }
}