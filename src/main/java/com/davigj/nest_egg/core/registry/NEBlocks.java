package com.davigj.nest_egg.core.registry;

import com.davigj.nest_egg.common.block.EmuNestBlock;
import com.davigj.nest_egg.core.NestEgg;
import com.davigj.nest_egg.core.other.NECompatUtil;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.incubation.common.block.BirdNestBlock;
import com.teamabnormals.incubation.common.block.EmptyNestBlock;
import com.teamabnormals.incubation.core.registry.IncubationBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static com.teamabnormals.incubation.core.registry.IncubationBlocks.HAY_NEST;
import static com.teamabnormals.incubation.core.registry.IncubationBlocks.TWIG_NEST;
import static net.minecraft.world.item.CreativeModeTabs.NATURAL_BLOCKS;
import static net.minecraft.world.item.crafting.Ingredient.of;

@Mod.EventBusSubscriber(modid = NestEgg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NEBlocks {
    public static final BlockSubRegistryHelper HELPER;
    public static final RegistryObject<Block> TWIG_EMU_NEST;
    public static final RegistryObject<Block> TWIG_BE_EMU_NEST;
    public static final RegistryObject<Block> TWIG_DUCK_NEST;
    public static final RegistryObject<Block> TWIG_PHEASANT_NEST;
    public static final RegistryObject<Block> TWIG_GE_DUCK_NEST;
    public static final RegistryObject<Block> HAY_EMU_NEST;
    public static final RegistryObject<Block> HAY_BE_EMU_NEST;
    public static final RegistryObject<Block> HAY_DUCK_NEST;
    public static final RegistryObject<Block> HAY_PHEASANT_NEST;
    public static final RegistryObject<Block> HAY_GE_DUCK_NEST;

    public static final RegistryObject<Block> EMU_EGG_CRATE;
    public static final RegistryObject<Block> BE_EMU_EGG_CRATE;
    public static final RegistryObject<Block> DUCK_EGG_CRATE;
    public static final RegistryObject<Block> PHEASANT_EGG_CRATE;
    public static final RegistryObject<Block> GE_DUCK_EGG_CRATE;

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
        TWIG_BE_EMU_NEST = HELPER.createBlockNoItem("twig_be_emu_nest", () -> {
            return new EmuNestBlock(() -> {
                return NECompatUtil.BEemuEgg;
            }, (EmptyNestBlock)TWIG_NEST.get(), IncubationBlocks.IncubationProperties.TWIG_NEST);
        });
        HAY_BE_EMU_NEST = HELPER.createBlockNoItem("hay_be_emu_nest", () -> {
            return new EmuNestBlock(() -> {
                return NECompatUtil.BEemuEgg;
            }, (EmptyNestBlock)HAY_NEST.get(), IncubationBlocks.IncubationProperties.HAY_NEST);
        });

        TWIG_DUCK_NEST = HELPER.createBlockNoItem("twig_duck_nest", () -> {
            return new BirdNestBlock(() -> {
                return NECompatUtil.naturalistDuckEgg;
            }, (EmptyNestBlock)TWIG_NEST.get(), IncubationBlocks.IncubationProperties.TWIG_NEST);
        });
        HAY_DUCK_NEST = HELPER.createBlockNoItem("hay_duck_nest", () -> {
            return new BirdNestBlock(() -> {
                return NECompatUtil.naturalistDuckEgg;
            }, (EmptyNestBlock)HAY_NEST.get(), IncubationBlocks.IncubationProperties.HAY_NEST);
        });

        TWIG_PHEASANT_NEST = HELPER.createBlockNoItem("twig_pheasant_nest", () -> {
            return new BirdNestBlock(() -> {
                return NECompatUtil.pheasantEgg;
            }, (EmptyNestBlock)TWIG_NEST.get(), IncubationBlocks.IncubationProperties.TWIG_NEST);
        });
        HAY_PHEASANT_NEST = HELPER.createBlockNoItem("hay_pheasant_nest", () -> {
            return new BirdNestBlock(() -> {
                return NECompatUtil.pheasantEgg;
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
        EMU_EGG_CRATE = HELPER.createBlock("emu_egg_crate", () -> {
            return new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLUE).strength(1.5F).sound(SoundType.WOOD));
        });
        BE_EMU_EGG_CRATE = HELPER.createBlock("be_emu_egg_crate", () -> {
            return new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLUE).strength(1.5F).sound(SoundType.WOOD));
        });
        DUCK_EGG_CRATE = HELPER.createBlock("duck_egg_crate", () -> {
            return new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).strength(1.5F).sound(SoundType.WOOD));
        });
        PHEASANT_EGG_CRATE = HELPER.createBlock("pheasant_egg_crate", () -> {
            return new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_PINK).strength(1.5F).sound(SoundType.WOOD));
        });
        GE_DUCK_EGG_CRATE = HELPER.createBlock("ge_duck_egg_crate", () -> {
            return new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).strength(1.5F).sound(SoundType.WOOD));
        });
    }

    public static void buildCreativeTabContents() {
        if (ModList.get().isLoaded("alexsmobs")) {
            CreativeModeTabContentsPopulator.mod(NestEgg.MOD_ID)
                    .tab(NATURAL_BLOCKS).addItemsAfter(of(Blocks.FLOWERING_AZALEA), EMU_EGG_CRATE)
            ;
        }
        if (ModList.get().isLoaded("naturalist")) {
            CreativeModeTabContentsPopulator.mod(NestEgg.MOD_ID)
                    .tab(NATURAL_BLOCKS).addItemsAfter(of(Blocks.FLOWERING_AZALEA), DUCK_EGG_CRATE)
            ;
        }
        if (ModList.get().isLoaded("geologicexpansion")) {
            CreativeModeTabContentsPopulator.mod(NestEgg.MOD_ID)
                    .tab(NATURAL_BLOCKS).addItemsAfter(of(Blocks.FLOWERING_AZALEA), GE_DUCK_EGG_CRATE)
            ;
        }
        if (ModList.get().isLoaded("bountiful_critters")) {
            CreativeModeTabContentsPopulator.mod(NestEgg.MOD_ID)
                    .tab(NATURAL_BLOCKS).addItemsAfter(of(Blocks.FLOWERING_AZALEA), BE_EMU_EGG_CRATE, PHEASANT_EGG_CRATE)
            ;
        }
    }
}