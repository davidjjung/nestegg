package com.davigj.nest_egg.core.other;

import com.davigj.nest_egg.core.NestEgg;
import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class NEItemTags {
    public static final TagKey<Item> SHORT_FEED = itemTag("short_feed");
    public static final TagKey<Item> LONG_FEED = itemTag("long_feed");

    private static TagKey<Item> itemTag(String name) {
        return TagUtil.itemTag(NestEgg.MOD_ID, name);
    }
}
