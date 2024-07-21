package com.davigj.nest_egg.core.registry;

import com.davigj.nest_egg.common.block.EmuNestBlock;
import com.davigj.nest_egg.core.NestEgg;
import com.davigj.nest_egg.core.other.NECompatUtil;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.incubation.common.block.BirdNestBlock;
import com.teamabnormals.incubation.common.block.EmptyNestBlock;
import com.teamabnormals.incubation.core.registry.IncubationBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static com.teamabnormals.incubation.core.registry.IncubationBlocks.HAY_NEST;
import static com.teamabnormals.incubation.core.registry.IncubationBlocks.TWIG_NEST;

@Mod.EventBusSubscriber(modid = NestEgg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NEBlocks {
    public static final BlockSubRegistryHelper HELPER;
    public static final RegistryObject<Block> TWIG_EMU_NEST;
    public static final RegistryObject<Block> TWIG_DUCK_NEST;
    public static final RegistryObject<Block> TWIG_GE_DUCK_NEST;
    public static final RegistryObject<Block> HAY_EMU_NEST;
    public static final RegistryObject<Block> HAY_DUCK_NEST;
    public static final RegistryObject<Block> HAY_GE_DUCK_NEST;

    static {
        HELPER = (BlockSubRegistryHelper) NestEgg.REGISTRY_HELPER.getBlockSubHelper();
        TWIG_EMU_NEST = HELPER.createBlockNoItem("twig_emu_nest", () -> {
            return new EmuNestBlock(() -> {
                return NECompatUtil.emuEgg;
            }, (EmptyNestBlock)TWIG_NEST.get(), IncubationBlocks.IncubationProperties.TWIG_NEST);
        });
        HAY_EMU_NEST = HELPER.createBlockNoItem("hay_emu_nest", () -> {
            return new EmuNestBlock(() -> {
                return NECompatUtil.emuEgg;
            }, (EmptyNestBlock)HAY_NEST.get(), IncubationBlocks.IncubationProperties.HAY_NEST);
        });

        TWIG_DUCK_NEST = HELPER.createBlockNoItem("twig_duck_nest", () -> {
            return new BirdNestBlock(() -> {
                return NECompatUtil.duckEgg;
            }, (EmptyNestBlock)TWIG_NEST.get(), IncubationBlocks.IncubationProperties.TWIG_NEST);
        });
        HAY_DUCK_NEST = HELPER.createBlockNoItem("hay_duck_nest", () -> {
            return new BirdNestBlock(() -> {
                return NECompatUtil.duckEgg;
            }, (EmptyNestBlock)HAY_NEST.get(), IncubationBlocks.IncubationProperties.HAY_NEST);
        });

        TWIG_GE_DUCK_NEST = HELPER.createBlockNoItem("twig_ge_duck_nest", () -> {
            return new BirdNestBlock(() -> {
                return NECompatUtil.GEduckEgg;
            }, (EmptyNestBlock)TWIG_NEST.get(), IncubationBlocks.IncubationProperties.TWIG_NEST);
        });
        HAY_GE_DUCK_NEST = HELPER.createBlockNoItem("hay_ge_duck_nest", () -> {
            return new BirdNestBlock(() -> {
                return NECompatUtil.GEduckEgg;
            }, (EmptyNestBlock)HAY_NEST.get(), IncubationBlocks.IncubationProperties.HAY_NEST);
        });
    }
}