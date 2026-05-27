package com.davigj.nest_egg.core.registry;

import com.davigj.nest_egg.common.block.EmuNestBlock;
import com.davigj.nest_egg.core.NestEgg;
import com.davigj.nest_egg.core.other.compat.AMCompat;
import com.davigj.nest_egg.core.other.compat.NaturalistCompat;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import com.teamabnormals.incubation.common.block.BirdNestBlock;
import com.teamabnormals.incubation.common.block.EmptyNestBlock;
import com.teamabnormals.incubation.core.registry.IncubationBlocks;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Supplier;

import static com.davigj.nest_egg.core.other.compat.ModConstants.ALEXSMOBS;
import static com.davigj.nest_egg.core.other.compat.ModConstants.NATURALIST;
import static com.teamabnormals.incubation.core.registry.IncubationBlocks.HAY_NEST;
import static com.teamabnormals.incubation.core.registry.IncubationBlocks.TWIG_NEST;
import static net.minecraft.world.item.CreativeModeTabs.NATURAL_BLOCKS;
import static net.minecraft.world.item.crafting.Ingredient.of;

public class NEBlocks {
    public static final BlockSubRegistryHelper BLOCKS;
    public static final ItemSubRegistryHelper ITEMS;
    public static final DeferredBlock<Block> TWIG_EMU_NEST;
    public static final DeferredBlock<Block> TWIG_DUCK_NEST;
    public static final DeferredBlock<Block> HAY_EMU_NEST;
    public static final DeferredBlock<Block> HAY_DUCK_NEST;

    public static final DeferredBlock<Block> EMU_EGG_CRATE;
    public static final DeferredBlock<Block> DUCK_EGG_CRATE;

    static {
        BLOCKS = NestEgg.REGISTRY_HELPER.getBlockSubHelper();
        ITEMS = NestEgg.REGISTRY_HELPER.getItemSubHelper();
        TWIG_EMU_NEST = BLOCKS.createBlockNoItem("twig_emu_nest", () -> {
            return new EmuNestBlock(() -> {
                return AMCompat.emuEgg;
            }, (EmptyNestBlock)TWIG_NEST.get(), IncubationBlocks.IncubationProperties.TWIG_NEST);
        });
        HAY_EMU_NEST = BLOCKS.createBlockNoItem("hay_emu_nest", () -> {
            return new EmuNestBlock(() -> {
                return AMCompat.emuEgg;
            }, (EmptyNestBlock)HAY_NEST.get(), IncubationBlocks.IncubationProperties.HAY_NEST);
        });

        TWIG_DUCK_NEST = BLOCKS.createBlockNoItem("twig_duck_nest", () -> {
            return new BirdNestBlock(() -> {
                return NaturalistCompat.naturalistDuckEgg;
            }, (EmptyNestBlock)TWIG_NEST.get(), IncubationBlocks.IncubationProperties.TWIG_NEST);
        });
        HAY_DUCK_NEST = BLOCKS.createBlockNoItem("hay_duck_nest", () -> {
            return new BirdNestBlock(() -> {
                return NaturalistCompat.naturalistDuckEgg;
            }, (EmptyNestBlock)HAY_NEST.get(), IncubationBlocks.IncubationProperties.HAY_NEST);
        });

        EMU_EGG_CRATE = BLOCKS.createBlock("emu_egg_crate", () -> {
            return new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLUE).strength(1.5F).sound(SoundType.WOOD));
        });
        DUCK_EGG_CRATE = BLOCKS.createBlock("duck_egg_crate", () -> {
            return new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).strength(1.5F).sound(SoundType.WOOD));
        });
    }

    public static void buildCreativeTabContents() {
        if (ALEXSMOBS) {
            CreativeModeTabContentsPopulator.mod(NestEgg.MOD_ID).tab(NATURAL_BLOCKS)
                    .addItemsAfter(of(Blocks.FLOWERING_AZALEA), EMU_EGG_CRATE);
        }
        if (NATURALIST) {
            CreativeModeTabContentsPopulator.mod(NestEgg.MOD_ID).tab(NATURAL_BLOCKS)
                    .addItemsAfter(of(Blocks.FLOWERING_AZALEA), DUCK_EGG_CRATE);
        }
    }

    public static void setupTabEditors() {
        if (ALEXSMOBS) {
            CreativeModeTabContentsPopulator.mod(NestEgg.MOD_ID).tab(CreativeModeTabs.NATURAL_BLOCKS)
                    .addItemsAfter(Ingredient.of(new ItemLike[]{Blocks.HAY_BLOCK}),
                            new Supplier[]{NEBlocks.EMU_EGG_CRATE});
        }
        if (NATURALIST) {
            CreativeModeTabContentsPopulator.mod(NestEgg.MOD_ID).tab(CreativeModeTabs.NATURAL_BLOCKS)
                    .addItemsAfter(Ingredient.of(new ItemLike[]{Blocks.HAY_BLOCK}),
                            new Supplier[]{NEBlocks.DUCK_EGG_CRATE});
        }
    }
}