package com.davigj.nest_egg.core.other;

import com.davigj.nest_egg.common.entity.ai.goal.EmuIncubateGoal;
import com.davigj.nest_egg.common.entity.ai.goal.IncubateGoal;
import com.davigj.nest_egg.common.entity.ai.goal.SideEyeNestGoal;
import com.davigj.nest_egg.common.entity.ai.goal.StealEggFromNestGoal;
import com.davigj.nest_egg.core.NEConfig;
import com.davigj.nest_egg.core.NestEgg;
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
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;

import static com.davigj.nest_egg.core.other.NECompatUtil.*;

@Mod.EventBusSubscriber(modid = NestEgg.MOD_ID)
public class NEEvents {
    // /summon alexsmobs:blue_jay ~ ~ ~ {HandItems:[{id:"stick",Count:1},{}], NoAI:true}
    static TrackedDataManager manager = TrackedDataManager.INSTANCE;

    @SubscribeEvent
    public static void nestWreckers(EntityJoinLevelEvent event) {
        if (!ModList.get().isLoaded("alexsmobs") || !NEConfig.COMMON.nestWrecker.get()) return;
        Entity entity = event.getEntity();
        if (isRaccoon(entity)) {
            ((Mob)(entity)).goalSelector.addGoal(7, new StealEggFromNestGoal((Animal)entity, 1.0D));
        }
    }

    @SubscribeEvent
    public static void sideEye(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!ModList.get().isLoaded("alexsmobs") || !NEConfig.COMMON.sideEye.get()) return;
        if (isCrow(entity)) {
            ((Mob)(entity)).goalSelector.addGoal(6, new SideEyeNestGoal((Animal)entity));
        }
    }

    @SubscribeEvent
    public static void birdAlert(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EggLayer eggLayer && NEConfig.COMMON.incubator.get()) {
            Animal bird = (Animal) entity;

            List<IncubateGoalAdder> handlers = List.of(
                    new IncubateGoalAdder("autumnity", bird, eggLayer, BirdType.TURKEY),
                    new IncubateGoalAdder("environmental", bird, eggLayer, BirdType.ENVIRONMENTAL_DUCK),
                    new IncubateGoalAdder("geologicexpansion", bird, eggLayer, BirdType.GE_DUCK),
                    new IncubateGoalAdder("naturalist", bird, eggLayer, BirdType.NATURALIST_DUCK),
                    new IncubateGoalAdder("alexsmobs", bird, eggLayer, BirdType.EMU),
                    new IncubateGoalAdder("bountiful_critters", bird, eggLayer, BirdType.BC_EMU),
                    new IncubateGoalAdder("bountiful_critters", bird, eggLayer, BirdType.PHEASANT)
            );

            for (IncubateGoalAdder handler : handlers) {
                if (handler.isValidBird()) {
                    handler.addGoal();
                    return;
                }
            }

            if (entity instanceof Chicken chicken) {
                chicken.goalSelector.addGoal(2, new IncubateGoal(eggLayer, 1.0D));
            }
        }
    }

    // Is it bad to make it an enum, it just looks so much neater imo
    enum BirdType {
        TURKEY,
        ENVIRONMENTAL_DUCK,
        GE_DUCK,
        NATURALIST_DUCK,
        EMU,
        BC_EMU,
        PHEASANT
    }

    static class IncubateGoalAdder {
        private final String modName;
        private final Animal bird;
        private final EggLayer eggLayer;
        private final BirdType birdType;

        IncubateGoalAdder(String modName, Animal bird, EggLayer eggLayer, BirdType birdType) {
            this.modName = modName;
            this.bird = bird;
            this.eggLayer = eggLayer;
            this.birdType = birdType;
        }

        boolean isValidBird() {
            return ModList.get().isLoaded(modName) && checkBirdType(birdType, bird);
        }

        void addGoal() {
            if (birdType == BirdType.EMU || birdType == BirdType.BC_EMU) {
                bird.goalSelector.addGoal(2, new EmuIncubateGoal(eggLayer, 1.0D));
            } else {
                bird.goalSelector.addGoal(2, new IncubateGoal(eggLayer, 1.0D));
            }
        }

        private boolean checkBirdType(BirdType type, Entity entity) {
            return switch (type) {
                case TURKEY -> isTurkey(entity);
                case ENVIRONMENTAL_DUCK -> isEnvironmentalDuck(entity);
                case GE_DUCK -> isGEDuck(entity);
                case NATURALIST_DUCK -> isNaturalistDuck(entity);
                case EMU -> isEmu(entity);
                case BC_EMU -> isBCEmu(entity);
                case PHEASANT -> isPheasant(entity);
            };
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
        if (isRaccoon(target)) {
            int hungy = manager.getValue(target, NestEgg.THIEF_TIMER);
            if (hungy > 0) {
                manager.setValue(target, NestEgg.THIEF_TIMER, hungy - 1);
            }
        }
        if (isCrow(target)) {
            int hungy = manager.getValue(target, NestEgg.THIEF_TIMER);
            if (hungy > 0) {
                manager.setValue(target, NestEgg.THIEF_TIMER, hungy - 1);
            }
            int flee = manager.getValue(target, NestEgg.FLEE_TIMER);
            if (flee > 0) {
                if (!isCrowFlying(target) && (target.onGround() || target.isInWater())) {
                    setNotFlying(target);
                }
                manager.setValue(target, NestEgg.FLEE_TIMER, flee - 1);
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

            if (ModList.get().isLoaded("alexsmobs")) {
                if (isEmu(parentB)) {
                    eggo.setEggTimer(400);
                    if (NEConfig.COMMON.incubator.get()) {
                        manager.setValue(parentB, NestEgg.EMU_INCUBATION_COOLDOWN, 0);
                        manager.setValue(parentA, NestEgg.EMU_INCUBATION_COOLDOWN, NEConfig.COMMON.emuIncubationCooldown.get());
                        if (NEConfig.COMMON.birdFeed.get()) {
                            manager.setValue(parentB, NestEgg.FEED_TIMER, NEConfig.COMMON.longTier.get());
                        }
                    }
                    return;
                }
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
}