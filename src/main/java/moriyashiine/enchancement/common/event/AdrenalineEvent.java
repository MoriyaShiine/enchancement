/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class AdrenalineEvent implements MultiplyMovementSpeedEvent {
	@Override
	public float multiply(float currentMultiplier, World world, LivingEntity living) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.ADRENALINE, living)) {
			currentMultiplier = EnchancementUtil.capMovementMultiplier(currentMultiplier * getMultiplier(living));
		}
		return currentMultiplier;
	}

	public static float getMultiplier(LivingEntity living) {
		float percentage = living.getHealth() / living.getMaxHealth();
		return switch ((int) Math.floor(percentage * 10 + 0.5)) {
			case 10 -> 1;
			case 9 -> 1.1F;
			case 8 -> 1.2F;
			case 7 -> 1.3F;
			case 6 -> 1.4F;
			case 5 -> 1.5F;
			case 4 -> 1.6F;
			case 3 -> 1.7F;
			default -> 1.8F;
		};
	}
}
