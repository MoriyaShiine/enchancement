/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {
	public static final ResourceKey<DamageType> AMETHYST_SHARD = ResourceKey.create(Registries.DAMAGE_TYPE, Enchancement.id("amethyst_shard"));
	public static final ResourceKey<DamageType> BRIMSTONE = ResourceKey.create(Registries.DAMAGE_TYPE, Enchancement.id("brimstone"));
	public static final ResourceKey<DamageType> ICE_SHARD = ResourceKey.create(Registries.DAMAGE_TYPE, Enchancement.id("ice_shard"));
	public static final ResourceKey<DamageType> LIFE_DRAIN = ResourceKey.create(Registries.DAMAGE_TYPE, Enchancement.id("life_drain"));

	public static void bootstrap(BootstrapContext<DamageType> registry) {
		registry.register(AMETHYST_SHARD, new DamageType("arrow", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
		registry.register(BRIMSTONE, new DamageType("arrow", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
		registry.register(ICE_SHARD, new DamageType("freeze", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0, DamageEffects.FREEZING));
		registry.register(LIFE_DRAIN, new DamageType("enchancement.life_drain", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0));
	}
}
