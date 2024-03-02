/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AdrenalineEvent implements MultiplyMovementSpeedEvent {
	@Override
	public float multiply(float currentMultiplier, World world, LivingEntity living) {
		int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.ADRENALINE, living);
		if (level > 0) {
			currentMultiplier = EnchancementUtil.capMovementMultiplier(currentMultiplier * getSpeedMultiplier(living, level));
		}
		return currentMultiplier;
	}

	public static float getSpeedMultiplier(LivingEntity living, int level) {
		return 1 + level * 0.05F * (10 - Math.max(3, getFlooredHealth(living)) + 1);
	}

	public static float getDamageMultiplier(LivingEntity living, int level) {
		float value = level * (0.2F / 14) * (10 - Math.max(3, AdrenalineEvent.getFlooredHealth(living)));
		return Math.max(0, 1 - MathHelper.ceil(value * 100) / 100F);
	}

	public static int getFlooredHealth(LivingEntity living) {
		float percentage = living.getHealth() / living.getMaxHealth();
		return (int) Math.floor(percentage * 10 + 0.5);
	}
}
