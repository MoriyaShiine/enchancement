/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ModDamageTypes {
	public static final RegistryKey<DamageType> AMETHYST_SHARD = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Enchancement.id("amethyst_shard"));
	public static final RegistryKey<DamageType> BRIMSTONE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Enchancement.id("brimstone"));
	public static final RegistryKey<DamageType> ICE_SHARD = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Enchancement.id("ice_shard"));
	public static final RegistryKey<DamageType> LIFE_DRAIN = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Enchancement.id("life_drain"));

	public static DamageSource create(World world, RegistryKey<DamageType> key, @Nullable Entity source, @Nullable Entity attacker) {
		return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key), source, attacker);
	}

	public static DamageSource create(World world, RegistryKey<DamageType> key, @Nullable Entity attacker) {
		return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key), attacker);
	}

	public static DamageSource create(World world, RegistryKey<DamageType> key) {
		return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
	}
}
