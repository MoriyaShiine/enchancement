/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.config;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.AirMobilityComponent;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.TickEntityEvent;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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

	public static class Efficiency implements TickEntityEvent {
		private static final AttributeModifier WEAK_EFFICIENCY = new AttributeModifier(Enchancement.id("toggleable_passive_efficiency"), 9, AttributeModifier.Operation.ADD_VALUE);
		private static final AttributeModifier STRONG_EFFICIENCY = new AttributeModifier(Enchancement.id("toggleable_passive_efficiency"), 25, AttributeModifier.Operation.ADD_VALUE);

		@Override
		public void tick(Level level, Entity entity) {
			if (entity instanceof ServerPlayer player) {
				ItemStack stack = player.getInventory().getItem(player.getInventory().getSelectedSlot());
				boolean hasEfficiency = hasEfficiency(stack);
				SLibUtils.conditionallyApplyAttributeModifier(player, Attributes.MINING_EFFICIENCY, EnchancementUtil.hasWeakEnchantments(stack) ? WEAK_EFFICIENCY : STRONG_EFFICIENCY, hasEfficiency);
			}
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
