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
		return 1 + switch ((int) Math.floor(percentage * 10 + 0.5)) {
			case 10 -> level * 0.05F;
			case 9 -> level * 0.1F;
			case 8 -> level * 0.15F;
			case 7 -> level * 0.2F;
			case 6 -> level * 0.25F;
			case 5 -> level * 0.3F;
			case 4 -> level * 0.35F;
			default -> level * 0.4F;
		};
	}
}
