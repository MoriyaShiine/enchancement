/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.tag.ModDamageTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagProvider extends FabricTagProvider<DamageType> {
	public ModDamageTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getOrCreateTagBuilder(ModDamageTypeTags.BYPASSES_WARDENSPINE)
				.addOptionalTag(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)
				.addOptionalTag(DamageTypeTags.BYPASSES_INVULNERABILITY);
		getOrCreateTagBuilder(ModDamageTypeTags.DOES_NOT_INTERRUPT)
				.addOptional(ModDamageTypes.LIFE_DRAIN)
				.addOptionalTag(DamageTypeTags.BYPASSES_INVULNERABILITY);

		getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR)
				.addOptional(ModDamageTypes.BRIMSTONE)
				.addOptional(ModDamageTypes.LIFE_DRAIN);
		getOrCreateTagBuilder(DamageTypeTags.BYPASSES_COOLDOWN)
				.addOptional(ModDamageTypes.AMETHYST_SHARD)
				.addOptional(ModDamageTypes.ICE_SHARD);
		getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ENCHANTMENTS)
				.addOptional(ModDamageTypes.BRIMSTONE)
				.addOptional(ModDamageTypes.LIFE_DRAIN);
		getOrCreateTagBuilder(DamageTypeTags.BYPASSES_RESISTANCE)
				.addOptional(ModDamageTypes.BRIMSTONE)
				.addOptional(ModDamageTypes.LIFE_DRAIN);
		getOrCreateTagBuilder(DamageTypeTags.IS_FREEZING)
				.addOptional(ModDamageTypes.ICE_SHARD);
		getOrCreateTagBuilder(DamageTypeTags.IS_PROJECTILE)
				.addOptional(ModDamageTypes.AMETHYST_SHARD)
				.addOptional(ModDamageTypes.BRIMSTONE)
				.addOptional(ModDamageTypes.ICE_SHARD);
		getOrCreateTagBuilder(DamageTypeTags.NO_IMPACT)
				.addOptional(ModDamageTypes.LIFE_DRAIN);
		getOrCreateTagBuilder(DamageTypeTags.NO_KNOCKBACK)
				.addOptional(ModDamageTypes.LIFE_DRAIN);
	}
}
