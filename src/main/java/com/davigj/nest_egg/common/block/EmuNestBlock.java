package com.davigj.nest_egg.common.block;

import com.davigj.nest_egg.common.block.entity.EmuNestBlockEntity;
import com.davigj.nest_egg.core.NEConfig;
import com.davigj.nest_egg.core.registry.NEBlockEntityTypes;
import com.github.alexthe666.alexsmobs.entity.EntityEmu;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.teamabnormals.incubation.common.block.EmptyNestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class EmuNestBlock extends BaseEntityBlock {
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
            return (Item) ForgeRegistries.ITEMS.getValue(eggIn);
        }, emptyNestIn, properties);
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (player.mayBuild()) {
            ItemStack itemstack = player.getItemInHand(handIn);
            if (this.egg.get() != Items.AIR && itemstack.getItem() == this.egg.get()) {
                return InteractionResult.CONSUME;
            } else {
                popResource(worldIn, pos, new ItemStack((ItemLike) this.egg.get()));
                this.removeEgg(worldIn, pos, state);
                if (ModList.get().isLoaded("alexsmobs") && NEConfig.COMMON.emuAggro.get()) {
                    int mercy = 3;
                    for (EntityEmu emu : worldIn.getEntitiesOfClass(EntityEmu.class, new AABB(pos).inflate(10))) {
                        if (!emu.isBaby() && mercy > 0) {
                            emu.playSound((SoundEvent) AMSoundRegistry.EMU_HURT.get(), 1.0F, emu.getVoicePitch());
                            emu.setTarget(player);
                            mercy--;
                        }
                    }
                }
                return InteractionResult.sidedSuccess(worldIn.isClientSide);
            }
        } else {
            return super.use(state, worldIn, pos, player, handIn, hit);
        }
    }

    private void removeEgg(Level world, BlockPos pos, BlockState state) {
        world.setBlock(pos, this.getEmptyNest().defaultBlockState(), 3);
    }

    public ItemStack getCloneItemStack(BlockGetter worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(this.getEgg());
    }

    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.below()).isSolid();
    }

    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(worldIn, pos, state, player);
        if (!worldIn.isClientSide && !player.isCreative() && this.getEgg() != null) {
            popResource(worldIn, pos, new ItemStack(this.getEgg(), 1));
        }

    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EmuNestBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, (BlockEntityType) NEBlockEntityTypes.EMU_NEST.get(), EmuNestBlockEntity::serverTick);
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
}
