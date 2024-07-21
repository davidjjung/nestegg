package com.davigj.nest_egg.core.registry;

import com.davigj.nest_egg.common.block.EmuNestBlock;
import com.davigj.nest_egg.common.block.entity.EmuNestBlockEntity;
import com.teamabnormals.blueprint.core.util.registry.BlockEntitySubRegistryHelper;
import com.teamabnormals.incubation.core.Incubation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

@Mod.EventBusSubscriber(
        modid = "nest_egg",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class NEBlockEntityTypes {
    public static final BlockEntitySubRegistryHelper HELPER;
    public static final RegistryObject<BlockEntityType<EmuNestBlockEntity>> EMU_NEST;

    static {
        HELPER = (BlockEntitySubRegistryHelper) Incubation.REGISTRY_HELPER.getBlockEntitySubHelper();
        EMU_NEST = HELPER.createBlockEntity("emu_nest", EmuNestBlockEntity::new, () -> {
            return Set.of(BlockEntitySubRegistryHelper.collectBlocks(EmuNestBlock.class));
        });
    }
}
