/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.tag.ModDamageTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends FabricTagsProvider<DamageType> {
	public ModDamageTypeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.DAMAGE_TYPE, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		builder(ModDamageTypeTags.BYPASSES_WARDENSPINE)
				.forceAddTag(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)
				.forceAddTag(DamageTypeTags.BYPASSES_INVULNERABILITY);
		builder(ModDamageTypeTags.DOES_NOT_INTERRUPT)
				.forceAddTag(DamageTypeTags.BYPASSES_INVULNERABILITY)
				.add(ModDamageTypes.LIFE_DRAIN);

		builder(DamageTypeTags.BYPASSES_ARMOR)
				.add(ModDamageTypes.BRIMSTONE)
				.add(ModDamageTypes.LIFE_DRAIN);
		builder(DamageTypeTags.BYPASSES_COOLDOWN)
				.add(ModDamageTypes.AMETHYST_SHARD)
				.add(ModDamageTypes.ICE_SHARD);
		builder(DamageTypeTags.BYPASSES_ENCHANTMENTS)
				.add(ModDamageTypes.BRIMSTONE)
				.add(ModDamageTypes.LIFE_DRAIN);
		builder(DamageTypeTags.BYPASSES_RESISTANCE)
				.add(ModDamageTypes.BRIMSTONE)
				.add(ModDamageTypes.LIFE_DRAIN);
		builder(DamageTypeTags.IS_FREEZING)
				.add(ModDamageTypes.ICE_SHARD);
		builder(DamageTypeTags.IS_PROJECTILE)
				.add(ModDamageTypes.AMETHYST_SHARD)
				.add(ModDamageTypes.BRIMSTONE)
				.add(ModDamageTypes.ICE_SHARD);
		builder(DamageTypeTags.NO_IMPACT)
				.add(ModDamageTypes.LIFE_DRAIN);
		builder(DamageTypeTags.NO_KNOCKBACK)
				.add(ModDamageTypes.LIFE_DRAIN);
	}
}
