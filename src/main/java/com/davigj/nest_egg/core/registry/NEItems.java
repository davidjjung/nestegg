package com.davigj.nest_egg.core.registry;

import com.davigj.nest_egg.core.NestEgg;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.world.item.crafting.Ingredient.of;

import static net.minecraft.world.item.CreativeModeTabs.FOOD_AND_DRINKS;

@Mod.EventBusSubscriber(modid = NestEgg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NEItems {
    public static final ItemSubRegistryHelper HELPER = NestEgg.REGISTRY_HELPER.getItemSubHelper();

//    public static final RegistryObject<Item> TEMPLATE_ITEM = HELPER.createItem("template_item", () -> new Item(new Item.Properties()));

    public static void buildCreativeTabContents() {
//        CreativeModeTabContentsPopulator.mod(NestEgg.MOD_ID)
//                .tab(FOOD_AND_DRINKS)
//                .addItemsAfter(of(Items.APPLE), TEMPLATE_ITEM);
    }
}
