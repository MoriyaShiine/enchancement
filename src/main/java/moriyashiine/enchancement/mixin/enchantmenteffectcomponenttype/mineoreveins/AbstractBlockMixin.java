/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.mineoreveins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.event.MineOreVeinsEvent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashSet;
import java.util.Set;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
	@ModifyReturnValue(method = "calcBlockBreakingDelta", at = @At("RETURN"))
	private float enchancement$mineOreVeins(float original, BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
		if (MineOreVeinsEvent.canActivate(player, player.getMainHandStack(), state)) {
			Set<BlockPos> ores = MineOreVeinsEvent.gatherOres(new HashSet<>(), player.getWorld(), new BlockPos.Mutable().set(pos), state.getBlock());
			if (MineOreVeinsEvent.isValid(ores, player.getMainHandStack())) {
				float mineOreVeinsSpeed = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.MINE_ORE_VEINS, player.getRandom(), player.getMainHandStack(), 0);
				return original * MathHelper.lerp(Math.min(1, ores.size() / 12F), mineOreVeinsSpeed, mineOreVeinsSpeed * 0.1F);
			}
		}
		return original;
	}
}
