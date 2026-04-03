/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.config;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.AirMobilityComponent;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.ModifyDestroySpeedEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class ToggleablePassivesEvent {
	public static class AirMobility implements MultiplyMovementSpeedEvent {
		@Override
		public float multiply(float currentMultiplier, Level level, LivingEntity living) {
			if (ModConfig.toggleablePassives && !living.onGround()) {
				AirMobilityComponent airMobilityComponent = ModEntityComponents.AIR_MOBILITY.getNullable(living);
				if (airMobilityComponent != null && airMobilityComponent.getTicksInAir() > 10) {
					return currentMultiplier * 1.5F;
				}
			}
			return currentMultiplier;
		}

		@Override
		public int getPriority() {
			return 1001;
		}
	}

	public static class Efficiency implements ModifyDestroySpeedEvent {
		@Override
		public float modify(Player player, ItemStack stack, Level level, BlockState state, @Nullable BlockPos pos) {
			if (hasEfficiency(stack)) {
				return EnchancementUtil.hasWeakEnchantments(stack) ? 9 : 25;
			}
			return 0;
		}

		private static boolean hasEfficiency(ItemStack stack) {
			if (ModConfig.toggleablePassives) {
				if (stack.is(ItemTags.MINING_ENCHANTABLE) && stack.getOrDefault(ModComponentTypes.TOGGLEABLE_PASSIVE, false)) {
					if (stack.isEnchanted()) {
						return true;
					}
					stack.remove(ModComponentTypes.TOGGLEABLE_PASSIVE);
				}
			}
			return false;
		}
	}
}
