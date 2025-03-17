/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.AirJumpComponent;
import moriyashiine.enchancement.common.component.entity.DirectionBurstComponent;
import moriyashiine.enchancement.common.component.entity.RotationBurstComponent;
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
			if (EnchantmentHelper.hasAnyEnchantmentsWith(currentStack, ModEnchantmentEffectComponentTypes.DIRECTION_BURST)) {
				DirectionBurstComponent directionBurstComponent = ModEntityComponents.DIRECTION_BURST.getNullable(livingEntity);
				if (directionBurstComponent != null) {
					directionBurstComponent.reset();
					directionBurstComponent.sync();
				}
			}
			if (EnchantmentHelper.hasAnyEnchantmentsWith(currentStack, ModEnchantmentEffectComponentTypes.ROTATION_BURST)) {
				RotationBurstComponent rotationBurstComponent = ModEntityComponents.ROTATION_BURST.getNullable(livingEntity);
				if (rotationBurstComponent != null) {
					rotationBurstComponent.reset();
					rotationBurstComponent.sync();
				}
			}
		}
	}
}
