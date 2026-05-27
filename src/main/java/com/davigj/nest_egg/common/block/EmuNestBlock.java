package com.davigj.nest_egg.common.block;

import com.davigj.nest_egg.core.NEConfig;
import com.davigj.nest_egg.core.other.compat.AMCompat;
import com.mojang.serialization.MapCodec;
import com.teamabnormals.incubation.common.block.BirdNestBlock;
import com.teamabnormals.incubation.common.block.EmptyNestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

import static com.davigj.nest_egg.core.other.compat.ModConstants.ALEXSMOBS;

public class EmuNestBlock extends Block implements WorldlyContainerHolder {
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);
    private final Supplier<? extends Item> egg;
    private final EmptyNestBlock emptyNest;

    public EmuNestBlock(Supplier<? extends Item> eggIn, EmptyNestBlock emptyNestIn, BlockBehaviour.Properties properties) {
        super(properties);
        this.egg = eggIn;
        this.emptyNest = emptyNestIn;
        this.emptyNest.addNest(this.egg, this);
        this.registerDefaultState((BlockState) ((BlockState) this.stateDefinition.any()));
    }

    public EmuNestBlock(ResourceLocation eggIn, EmptyNestBlock emptyNestIn, BlockBehaviour.Properties properties) {
        this(() -> {
            return (Item) BuiltInRegistries.ITEM.get(eggIn);
        }, emptyNestIn, properties);
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (player.mayBuild()) {
            ItemStack itemstack = player.getItemInHand(handIn);
            if (this.egg.get() != Items.AIR && itemstack.getItem() == this.egg.get()) {
                return ItemInteractionResult.CONSUME;
            } else {
                popResource(level, pos, new ItemStack((ItemLike) this.egg.get()));
                removeEgg(level, pos, player);
                if (ALEXSMOBS && NEConfig.COMMON.emuAggro.get()) {
                    AMCompat.angryEmus(level, pos, player);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        } else {
            return super.useItemOn(stack, state, level, pos, player, handIn, hit);
        }
    }

    private void removeEgg(Level level, BlockPos pos, Entity entity) {
        BlockState newState = this.getEmptyNest().defaultBlockState();
        level.setBlock(pos, newState, 3);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, newState));
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return new ItemStack(this.getEgg());
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && !player.isCreative() && this.getEgg() != null) {
            popResource(level, pos, new ItemStack(this.getEgg(), 1));
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public Item getEgg() {
        return (Item) this.egg.get();
    }

    public EmptyNestBlock getEmptyNest() {
        return this.emptyNest;
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return 1;
    }

    @Override
    public WorldlyContainer getContainer(BlockState state, LevelAccessor level, BlockPos pos) {
        return new BirdNestBlock.OutputContainer(state, level, pos, new ItemStack(this.getEgg()));
    }
}
