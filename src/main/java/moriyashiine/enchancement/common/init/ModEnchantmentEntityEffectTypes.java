/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.enchantment.effect.entity.*;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerEnchantmentEntityEffectType;

public class ModEnchantmentEntityEffectTypes {
	public static void init() {
		registerEnchantmentEntityEffectType("automate_eating", AutomateEatingEnchantmentEffect.CODEC);
		registerEnchantmentEntityEffectType("bury", BuryEffect.CODEC);
		registerEnchantmentEntityEffectType("conditional_attribute", ConditionalAttributeEnchantmentEffect.CODEC);
		registerEnchantmentEntityEffectType("extinguish", ExtinguishEnchantmentEffect.CODEC);
		registerEnchantmentEntityEffectType("freeze", FreezeEnchantmentEffect.CODEC);
		registerEnchantmentEntityEffectType("heal", HealEnchantmentEffect.CODEC);
		registerEnchantmentEntityEffectType("set_extended_water_time", SetExtendedWaterTimeEffect.CODEC);
		registerEnchantmentEntityEffectType("smash", SmashEffect.CODEC);
		registerEnchantmentEntityEffectType("spawn_particles_with_count", SpawnParticlesWithCountEnchantmentEffect.CODEC);
	}
}
