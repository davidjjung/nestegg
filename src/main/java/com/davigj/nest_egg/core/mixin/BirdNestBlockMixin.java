package com.davigj.nest_egg.core.mixin;

import com.davigj.nest_egg.core.other.NECompatUtil;
import com.teamabnormals.incubation.common.block.BirdNestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BirdNestBlock.class)
public class BirdNestBlockMixin {

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lcom/teamabnormals/incubation/common/block/BirdNestBlock;popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V"))
    private void angerTurkeys(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        NECompatUtil.angryTurkeys((BirdNestBlock)(Object)this, worldIn, pos, player);
    }
}
