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
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class ToggleablePassivesEvent {
	public static class AirMobility implements MultiplyMovementSpeedEvent {
		@Override
		public float multiply(float currentMultiplier, World world, LivingEntity living) {
			if (ModConfig.toggleablePassives && !living.isOnGround()) {
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
		private static final EntityAttributeModifier WEAK_EFFICIENCY = new EntityAttributeModifier(Enchancement.id("toggleable_passive_efficiency"), 9, EntityAttributeModifier.Operation.ADD_VALUE);
		private static final EntityAttributeModifier STRONG_EFFICIENCY = new EntityAttributeModifier(Enchancement.id("toggleable_passive_efficiency"), 25, EntityAttributeModifier.Operation.ADD_VALUE);

		@Override
		public void tick(ServerWorld world, Entity entity) {
			if (entity instanceof PlayerEntity player) {
				ItemStack stack = player.getInventory().getStack(player.getInventory().getSelectedSlot());
				boolean hasEfficiency = hasEfficiency(stack);
				SLibUtils.conditionallyApplyAttributeModifier(player, EntityAttributes.MINING_EFFICIENCY, EnchancementUtil.hasWeakEnchantments(stack) ? WEAK_EFFICIENCY : STRONG_EFFICIENCY, hasEfficiency);
			}
		}

		private static boolean hasEfficiency(ItemStack stack) {
			if (ModConfig.toggleablePassives) {
				if (stack.isIn(ItemTags.MINING_ENCHANTABLE) && stack.getOrDefault(ModComponentTypes.TOGGLEABLE_PASSIVE, false)) {
					if (stack.hasEnchantments()) {
						return true;
					}
					stack.remove(ModComponentTypes.TOGGLEABLE_PASSIVE);
				}
			}
			return false;
		}
	}
}
