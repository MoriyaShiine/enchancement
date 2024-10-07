/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.component.entity.AirJumpComponent;
import moriyashiine.enchancement.common.component.entity.DirectionMovementBurstComponent;
import moriyashiine.enchancement.common.component.entity.RotationMovementBurstComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class EquipmentResetEvent implements ServerEntityEvents.EquipmentChange {
	@Override
	public void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
		if (equipmentSlot.isArmorSlot()) {
			if (EnchantmentHelper.hasAnyEnchantmentsWith(currentStack, ModEnchantmentEffectComponentTypes.AIR_JUMP)) {
				AirJumpComponent airJumpComponent = ModEntityComponents.AIR_JUMP.getNullable(livingEntity);
				if (airJumpComponent != null) {
					airJumpComponent.reset();
					airJumpComponent.sync();
				}
			}
			if (EnchantmentHelper.hasAnyEnchantmentsWith(currentStack, ModEnchantmentEffectComponentTypes.DIRECTION_MOVEMENT_BURST)) {
				DirectionMovementBurstComponent directionMovementBurstComponent = ModEntityComponents.DIRECTION_MOVEMENT_BURST.getNullable(livingEntity);
				if (directionMovementBurstComponent != null) {
					directionMovementBurstComponent.reset();
					directionMovementBurstComponent.sync();
				}
			}
			if (EnchantmentHelper.hasAnyEnchantmentsWith(currentStack, ModEnchantmentEffectComponentTypes.ROTATION_MOVEMENT_BURST)) {
				RotationMovementBurstComponent rotationMovementBurstComponent = ModEntityComponents.ROTATION_MOVEMENT_BURST.getNullable(livingEntity);
				if (rotationMovementBurstComponent != null) {
					rotationMovementBurstComponent.reset();
					rotationMovementBurstComponent.sync();
				}
			}
		}
	}
}
