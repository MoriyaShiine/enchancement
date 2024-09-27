/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.component.entity.AirJumpComponent;
import moriyashiine.enchancement.common.enchantment.effect.AirJumpEffect;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class AirJumpEvent implements ServerEntityEvents.EquipmentChange {
	@Override
	public void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
		if (equipmentSlot.isArmorSlot() && EnchancementUtil.hasAnyEnchantmentsWith(livingEntity, ModEnchantmentEffectComponentTypes.AIR_JUMP)) {
			AirJumpComponent airJumpComponent = ModEntityComponents.AIR_JUMP.getNullable(livingEntity);
			if (airJumpComponent != null) {
				airJumpComponent.setCooldown(AirJumpEffect.getChargeCooldown(livingEntity));
				airJumpComponent.setJumpsLeft(0);
				airJumpComponent.sync();
			}
		}
	}
}
