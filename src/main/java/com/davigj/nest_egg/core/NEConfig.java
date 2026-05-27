package com.davigj.nest_egg.core;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class NEConfig {
    public static class Common {
        public final ModConfigSpec.ConfigValue<Integer> thiefTimer;
        public final ModConfigSpec.ConfigValue<Integer> thiefTimerBonus;
        public final ModConfigSpec.ConfigValue<Integer> incubationTime;
        public final ModConfigSpec.ConfigValue<Integer> emuIncubationTime;
        public final ModConfigSpec.ConfigValue<Integer> emuIncubationCooldown;
        public final ModConfigSpec.ConfigValue<Integer> incubationCooldown;
        public final ModConfigSpec.ConfigValue<Boolean> nestWrecker;
        public final ModConfigSpec.ConfigValue<Boolean> sideEye;
        public final ModConfigSpec.ConfigValue<Boolean> incubator;
        public final ModConfigSpec.ConfigValue<Boolean> ambient;
        public final ModConfigSpec.ConfigValue<Boolean> turkeyAggro;
        public final ModConfigSpec.ConfigValue<Boolean> emuAggro;
        public final ModConfigSpec.ConfigValue<Boolean> birdFeed;
        public final ModConfigSpec.ConfigValue<Boolean> eggBirth;
        public final ModConfigSpec.ConfigValue<Boolean> feedTiers;
        public final ModConfigSpec.ConfigValue<Boolean> noEggSpam;
        public final ModConfigSpec.ConfigValue<Integer> shortTier;
        public final ModConfigSpec.ConfigValue<Integer> longTier;

        Common (ModConfigSpec.Builder builder) {
            builder.push("changes");
            builder.push("experimental");
            noEggSpam = builder.comment("Egg layers no longer drop egg item entities periodically").define("No more egg spam", false);
            eggBirth = builder.comment("Egg layers that enter love mode try to lay eggs or incubate instead of producing live young").define("Breeding for eggs", false);
            sideEye = builder.comment("Crows from Alex's Mobs occasionally watch bird nests for stray egg item entities").define("Crow side eye", false);
            builder.pop();
            builder.push("incubation_behavior");
            incubator = builder.comment("Birds occasionally incubate their nests").define("Nest incubator", true);
            ambient = builder.comment("Birds don't hatch eggs, only sitting on them ambiently").define("Ambient incubator", false);
            incubationTime = builder.comment("Time spent incubating a nest").defineInRange("Incubation timer", 200, 150, 1000);
            incubationCooldown = builder.comment("Cooldown between successful hatchings").define("Incubation cooldown", 12000);
            emuIncubationTime = builder.comment("Time spent incubating an emu nest").define("Emu incubation timer", 300);
            emuIncubationCooldown = builder.comment("Cooldown in-between successful emu hatchings").define("Emu incubation cooldown", 24000);
            builder.push("bird_feed");
            birdFeed = builder.comment("Birds must be fed something special in order to attempt incubation").define("Incubation requires special food", true);
            feedTiers = builder.comment("There are two tiers of feed, which enable incubation for different durations." +
                    "\nSetting this to false will have all feed default to long feed duration").define("Incubation feed tiers", true);
            shortTier = builder.comment("Duration for items in nest_egg:short_feed").define("Short feed duration", 24000);
            longTier = builder.comment("Duration for items in nest_egg:long_feed.\nSetting to -1 makes it infinite").define("Long feed duration", 48000);
            builder.pop();
            builder.pop();
            builder.push("egg_stealing_behavior");
            turkeyAggro = builder.comment("Turkeys target entities that try raiding turkey egg nests").define("Turkey aggro", true);
            emuAggro = builder.comment("Emus target players who try raiding emu egg nests").define("Emu aggro", true);
            nestWrecker = builder.comment("Raccoons from Alex's Mobs occasionally raid bird nests").define("Nest wrecker", true);
            thiefTimer = builder.comment("How many ticks between attempts at stealing eggs, minimum").define("Thievery timer", 1200);
            thiefTimerBonus = builder.comment("Extra maximum ticks to add for variation").define("Thievery timer addition", 1200);
            builder.pop();
            builder.pop();
        }
    }

    static final ModConfigSpec COMMON_SPEC;
    public static final NEConfig.Common COMMON;


    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(NEConfig.Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}
