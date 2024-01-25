/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class AdrenalineEvent implements MultiplyMovementSpeedEvent {
	@Override
	public float multiply(float currentMultiplier, World world, LivingEntity living) {
		int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.ADRENALINE, living);
		if (level > 0) {
			currentMultiplier = EnchancementUtil.capMovementMultiplier(currentMultiplier * getMultiplier(living, level));
		}
		return currentMultiplier;
	}

	public static float getMultiplier(LivingEntity living, int level) {
		float percentage = living.getHealth() / living.getMaxHealth();
		return switch ((int) Math.floor(percentage * 10 + 0.5)) {
			case 10 -> level == 1 ? 1.05F : 1.1F;
			case 9 -> level == 1 ? 1.1F : 1.2F;
			case 8 -> level == 1 ? 1.15F : 1.3F;
			case 7 -> level == 1 ? 1.2F : 1.4F;
			case 6 -> level == 1 ? 1.25F : 1.5F;
			case 5 -> level == 1 ? 1.3F : 1.6F;
			case 4 -> level == 1 ? 1.35F : 1.7F;
			default -> level == 1 ? 1.4F : 1.8F;
		};
	}
}
