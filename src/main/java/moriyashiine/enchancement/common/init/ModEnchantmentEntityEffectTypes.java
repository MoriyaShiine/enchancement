/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.enchantment.effect.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEnchantmentEntityEffectTypes {
	public static void init() {
		Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Enchancement.id("automate_eating"), AutomateEatingEnchantmentEffect.CODEC);
		Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Enchancement.id("bury"), BuryEffect.CODEC);
		Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Enchancement.id("conditional_attribute"), ConditionalAttributeEnchantmentEffect.CODEC);
		Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Enchancement.id("extinguish"), ExtinguishEnchantmentEffect.CODEC);
		Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Enchancement.id("freeze"), FreezeEnchantmentEffect.CODEC);
		Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Enchancement.id("heal"), HealEnchantmentEffect.CODEC);
		Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Enchancement.id("set_extended_water_time"), SetExtendedWaterTimeEffect.CODEC);
		Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Enchancement.id("smash"), SmashEffect.CODEC);
		Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Enchancement.id("spawn_particles_with_count"), SpawnParticlesWithCountEnchantmentEffect.CODEC);
	}
}
