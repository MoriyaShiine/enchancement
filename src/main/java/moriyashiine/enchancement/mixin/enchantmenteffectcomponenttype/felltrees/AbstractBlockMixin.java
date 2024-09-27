/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.felltrees;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.event.FellTreesEvent;
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

import java.util.ArrayList;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
	@ModifyReturnValue(method = "calcBlockBreakingDelta", at = @At("RETURN"))
	private float enchancement$fellTrees(float original, BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
		if (FellTreesEvent.canActivate(player, player.getMainHandStack(), state)) {
			FellTreesEvent.Entry entry = FellTreesEvent.Entry.get(player);
			if (entry == null) {
				entry = new FellTreesEvent.Entry(player, FellTreesEvent.gatherTree(new ArrayList<>(), world, new BlockPos.Mutable().set(pos), state.getBlock()));
				FellTreesEvent.ENTRIES.add(entry);
			}
			if (FellTreesEvent.isValid(entry.tree())) {
				float fellTreesSpeed = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.FELL_TREES, player.getRandom(), player.getMainHandStack(), 0);
				return original * MathHelper.lerp(Math.min(1, entry.tree().size() / 32F), fellTreesSpeed, fellTreesSpeed * 0.05F);
			}
		}
		return original;
	}
}
