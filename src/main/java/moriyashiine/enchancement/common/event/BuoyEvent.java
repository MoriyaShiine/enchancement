/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class BuoyEvent implements MultiplyMovementSpeedEvent {
	@Override
	public float multiply(float currentMultiplier, World world, LivingEntity living) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BUOY, living)) {
			if (ModEntityComponents.EXTENDED_WATER.get(living).getTicksWet() > 0 || EnchancementUtil.isSubmerged(living, true, false, false)) {
				currentMultiplier = EnchancementUtil.capMovementMultiplier(currentMultiplier * 1.5F);
				int depthStriderLevel = EnchantmentHelper.getDepthStrider(living);
				while (depthStriderLevel > 0) {
					depthStriderLevel--;
					currentMultiplier = EnchancementUtil.capMovementMultiplier(currentMultiplier * 1.2F);
				}
			}
		}
		return currentMultiplier;
	}
}
