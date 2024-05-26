/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.util.SubmersionGate;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class BuoyEvent implements MultiplyMovementSpeedEvent {
	@Override
	public float multiply(float currentMultiplier, World world, LivingEntity living) {
		int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.BUOY, living);
		if (level > 0) {
			if (ModEntityComponents.EXTENDED_WATER.get(living).getTicksWet() > 0 || EnchancementUtil.isSubmerged(living, SubmersionGate.WATER_ONLY)) {
				currentMultiplier = EnchancementUtil.capMovementMultiplier(currentMultiplier * (1 + level * 0.75F));
			}
		}
		return currentMultiplier;
	}
}
