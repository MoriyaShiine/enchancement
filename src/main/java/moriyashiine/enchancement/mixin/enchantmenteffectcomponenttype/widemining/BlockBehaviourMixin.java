/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.widemining;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.component.level.WideMiningComponent;
import moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype.WideMiningEvent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModLevelComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {
	@ModifyExpressionValue(method = "getDestroyProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroySpeed(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
	private float enchancement$wideMining(float original, BlockState state, Player player, BlockGetter level, BlockPos pos) {
		if (WideMiningEvent.canActivate(player, player.getMainHandItem(), state)) {
			WideMiningComponent.Entry entry = ModLevelComponents.WIDE_MINING.get(level).getEntry(player);
			if (entry != null && WideMiningEvent.isValid(entry.blocks(), player.getMainHandItem())) {
				float wideMiningMultiplier = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.WIDE_MINING, player.getRandom(), player.getMainHandItem(), 0);
				return (original + entry.destroySpeed()) / wideMiningMultiplier;
			}
		}
		return original;
	}
}
