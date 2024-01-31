/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.component.entity.GaleComponent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class GaleEvent implements ServerEntityEvents.EquipmentChange {
	@Override
	public void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.GALE, currentStack)) {
			GaleComponent galeComponent = ModEntityComponents.GALE.getNullable(livingEntity);
			if (galeComponent != null) {
				galeComponent.setGaleCooldown(GaleComponent.DEFAULT_GALE_COOLDOWN);
				galeComponent.setJumpsLeft(0);
				galeComponent.sync();
			}
		}
	}
}
