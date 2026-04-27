/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.util.PushComponent;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.ladysnake.cca.api.v3.component.ComponentKey;

public class EquipmentResetEvent implements ServerEntityEvents.EquipmentChange {
	@Override
	public void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
		if (equipmentSlot.isArmor()) {
			for (ComponentKey<?> key : livingEntity.asComponentProvider().getComponentContainer().keys()) {
				if (livingEntity.getComponent(key) instanceof PushComponent pushComponent && EnchantmentHelper.has(currentStack, pushComponent.getEffectType())) {
					pushComponent.resetNextTick();
				}
			}
		}
	}
}
