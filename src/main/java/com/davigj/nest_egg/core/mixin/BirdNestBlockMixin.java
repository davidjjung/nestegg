package com.davigj.nest_egg.core.mixin;

import com.davigj.nest_egg.core.other.compat.AutumnityCompat;
import com.teamabnormals.incubation.common.block.BirdNestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.davigj.nest_egg.core.other.compat.ModConstants.AUTUMNITY;

@Mixin(BirdNestBlock.class)
public class BirdNestBlockMixin {

    @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lcom/teamabnormals/incubation/common/block/BirdNestBlock;popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V"))
    private void angerTurkeys(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (AUTUMNITY) {
            AutumnityCompat.angryTurkeys((BirdNestBlock)(Object)this, level, pos, player);
        }
    }
}
