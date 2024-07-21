package com.davigj.nest_egg.core.other;

import com.blackgear.geologicexpansion.common.entity.duck.Duck;
import com.blackgear.geologicexpansion.common.registries.GEItems;
import com.davigj.nest_egg.common.entity.ai.goal.*;
import com.davigj.nest_egg.core.NEConfig;
import com.davigj.nest_egg.core.NestEgg;
import com.github.alexthe666.alexsmobs.entity.EntityCrow;
import com.github.alexthe666.alexsmobs.entity.EntityEmu;
import com.github.alexthe666.alexsmobs.entity.EntityRaccoon;
import com.github.alexthe666.alexsmobs.entity.ai.ShoebillAIFlightFlee;
import com.starfish_studios.naturalist.core.registry.NaturalistItems;
import com.teamabnormals.autumnity.common.entity.animal.Turkey;
import com.teamabnormals.autumnity.core.registry.AutumnityItems;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.blueprint.core.api.EggLayer;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = NestEgg.MOD_ID)
public class NEEvents {
    // /summon alexsmobs:blue_jay ~ ~ ~ {HandItems:[{id:"stick",Count:1},{}], NoAI:true}
    static TrackedDataManager manager = TrackedDataManager.INSTANCE;

    @SubscribeEvent
    public static void nestWreckers(EntityJoinLevelEvent event) {
        if (!ModList.get().isLoaded("alexsmobs") || !NEConfig.COMMON.nestWrecker.get()) return;
        Entity entity = event.getEntity();
        if (entity instanceof EntityRaccoon raccoon) {
            raccoon.goalSelector.addGoal(7, new StealEggFromNestGoal(raccoon, 1.0D));
        }
    }

    @SubscribeEvent
    public static void sideEye(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!ModList.get().isLoaded("alexsmobs") || !NEConfig.COMMON.sideEye.get()) return;
        if (entity instanceof EntityCrow crow) {
            crow.goalSelector.addGoal(6, new SideEyeNestGoal(crow));
        }
    }

    @SubscribeEvent
    public static void birdAlert(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EggLayer eggLayer && NEConfig.COMMON.incubator.get()) {
            if (entity instanceof Chicken chicken) {
                chicken.goalSelector.addGoal(2, new IncubateGoal(eggLayer, 1.0D, Items.EGG));
            }
            if (ModList.get().isLoaded("autumnity")) {
                if (entity instanceof Turkey turkey) {
                    turkey.goalSelector.addGoal(2, new IncubateGoal(eggLayer, 1.0D, AutumnityItems.TURKEY_EGG.get()));
                    return;
                }
            }
            if (ModList.get().isLoaded("geologicexpansion")) {
                if (entity instanceof Duck duck) {
                    duck.goalSelector.addGoal(2, new IncubateGoal(eggLayer, 1.0D, GEItems.DUCK_EGG.get()));
                    return;
                }
            }
            if (ModList.get().isLoaded("naturalist")) {
                if (entity instanceof com.starfish_studios.naturalist.common.entity.Duck duck) {
                    duck.goalSelector.addGoal(2, new IncubateGoal(eggLayer, 1.0D, NaturalistItems.DUCK_EGG.get()));
                }
            }
            if (ModList.get().isLoaded("alexsmobs")) {
                if (entity instanceof EntityEmu emu) {
                    emu.goalSelector.addGoal(2, new EmuIncubateGoal(eggLayer, 1.0D));
                }
            }
        }
    }

    @SubscribeEvent
    public static void entityTick(LivingEvent.LivingTickEvent event) {
        Entity target = event.getEntity();
        if (target instanceof EggLayer bird) {
            int incubating = manager.getValue((Entity) bird, NestEgg.INCUBATION_COOLDOWN);
            if (incubating > 0) {
                manager.setValue((Entity) bird, NestEgg.INCUBATION_COOLDOWN, incubating - 1);
            }
            int feed = manager.getValue((Entity) bird, NestEgg.FEED_TIMER);
            if (feed > 0) {
                manager.setValue((Entity) bird, NestEgg.FEED_TIMER, feed - 1);
            }
            if (NEConfig.COMMON.noEggSpam.get() && bird.getEggTimer() - 20 <= 0) {
                bird.setEggTimer(400);
            }
        }
        if (!ModList.get().isLoaded("alexsmobs")) return;
        if (target instanceof EntityRaccoon raccoon) {
            int hungy = manager.getValue(raccoon, NestEgg.THIEF_TIMER);
            if (hungy > 0) {
                manager.setValue(raccoon, NestEgg.THIEF_TIMER, hungy - 1);
            }
        }
        if (target instanceof EntityCrow crow) {
            int hungy = manager.getValue(crow, NestEgg.THIEF_TIMER);
            if (hungy > 0) {
                manager.setValue(crow, NestEgg.THIEF_TIMER, hungy - 1);
            }
            int flee = manager.getValue(crow, NestEgg.FLEE_TIMER);
            if (flee > 0) {
                if (!crow.isFlying() && (crow.onGround() || crow.isInWater())) {
                    crow.setFlying(false);
                }
                manager.setValue(crow, NestEgg.FLEE_TIMER, flee - 1);
            }
        }
    }

    @SubscribeEvent
    public static void feedTheBeast(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof EggLayer && NEConfig.COMMON.birdFeed.get() && !((Animal) event.getTarget()).isBaby()) {
            ItemStack stack = event.getItemStack();
            Player player = event.getEntity();
            boolean shortFeed = stack.is(NEItemTags.SHORT_FEED);
            boolean longFeed = stack.is(NEItemTags.LONG_FEED);
            if (!(shortFeed || longFeed) || manager.getValue(event.getTarget(), NestEgg.FEED_TIMER) != 0) return;
            event.setCancellationResult(InteractionResult.CONSUME);
            ItemStack container = stack.getCraftingRemainingItem();
            Animal animal = (Animal) event.getTarget();
            animal.playSound(SoundEvents.GENERIC_EAT, 0.75F, 1.3F);
            for (int i = 0; i < 5; ++i) {
                double d0 = animal.getRandom().nextGaussian() * 0.05D;
                double d1 = animal.getRandom().nextGaussian() * 0.05D;
                double d2 = animal.getRandom().nextGaussian() * 0.05D;
                animal.level().addParticle(ParticleTypes.WAX_ON, animal.getRandomX(1.0D), animal.getRandomY() + 0.5D, animal.getRandomZ(1.0D), d0, d1, d2);
            }
            if (player != null) {
                player.swing(event.getHand());
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        player.setItemInHand(event.getHand(), container);
                    } else if (!player.getInventory().add(container)) {
                        player.drop(container, false);
                    }
                }
            }
            if (!NEConfig.COMMON.feedTiers.get() || longFeed) {
                manager.setValue(event.getTarget(), NestEgg.FEED_TIMER, NEConfig.COMMON.longTier.get());
                return;
            }
            manager.setValue(event.getTarget(), NestEgg.FEED_TIMER, NEConfig.COMMON.shortTier.get());
        }
    }

    @SubscribeEvent
    public static void noMoreLiveYoung(BabyEntitySpawnEvent event) {
        Mob mobA = event.getParentA();
        if (mobA instanceof EggLayer eggo && NEConfig.COMMON.eggBirth.get()) {
            Animal parentA = (Animal)mobA;
            Animal parentB = (Animal)event.getParentB();

            event.setCanceled(true);

            Optional.ofNullable(parentA.getLoveCause()).or(() -> {
                return Optional.ofNullable(parentB.getLoveCause());
            }).ifPresent((p_277486_) -> {
                p_277486_.awardStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(p_277486_, parentA, parentB, event.getChild());
            });

            if (parentA.level() instanceof ServerLevel server) {
                server.broadcastEntityEvent(parentA, (byte)18);
                if (server.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                    server.addFreshEntity(new ExperienceOrb(server, parentA.getX(), parentA.getY(), parentA.getZ(), parentA.getRandom().nextInt(7) + 1));
                }
            }

            if (ModList.get().isLoaded("alexsmobs") && parentB instanceof EntityEmu emu) {
                eggo.setEggTimer(400);
                if (NEConfig.COMMON.incubator.get()) {
                    manager.setValue(emu, NestEgg.EMU_INCUBATION_COOLDOWN, 0);
                    manager.setValue(parentA, NestEgg.EMU_INCUBATION_COOLDOWN, NEConfig.COMMON.emuIncubationCooldown.get());
                    if (NEConfig.COMMON.birdFeed.get()) {
                        manager.setValue(emu, NestEgg.FEED_TIMER, NEConfig.COMMON.longTier.get());
                    }
                }
                return;
            }
            eggo.setEggTimer(200);
            if (NEConfig.COMMON.incubator.get()) {
                manager.setValue(parentA, NestEgg.INCUBATION_COOLDOWN, NEConfig.COMMON.emuIncubationCooldown.get());
                manager.setValue(parentB, NestEgg.INCUBATION_COOLDOWN, 80);
                if (NEConfig.COMMON.birdFeed.get()) {
                    manager.setValue(parentB, NestEgg.FEED_TIMER, NEConfig.COMMON.longTier.get());
                }
            }
        }
    }

    @SubscribeEvent
    public static void optionalDatapack(AddPackFindersEvent event) {
//        Path resourcePath = ModList.get().getModFileById(NestEgg.MOD_ID).getFile().findResource("datapacks/" + id.getPath());
//        Pack pack = Pack.readMetaAndCreate(id.toString(), Component.literal(id.getNamespace() + "/" + id.getPath()), false, packId -> new PathPackResources(packId, true, resourcePath), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN);
//        if (pack == null) {
//            Veil.LOGGER.error("Failed to find builtin pack: {}", id);
//            return;
//        }
//        event.addRepositorySource(packConsumer -> packConsumer.accept(pack));
    }
}