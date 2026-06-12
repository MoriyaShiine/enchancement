/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.common.references.EnchancementEntityTypeIds;
import moriyashiine.enchancement.common.tag.EnchancementEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityTypeIds;

import java.util.concurrent.CompletableFuture;

public class EnchancementEntityTypeTagsProvider extends FabricTagsProvider.EntityTypeTagsProvider {
	public EnchancementEntityTypeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		builder(EnchancementEntityTypeTags.BRIMSTONE_HITTABLE)
				.forceAddTag(ConventionalEntityTypeTags.BOATS)
				.forceAddTag(ConventionalEntityTypeTags.MINECARTS)
				.add(EntityTypeIds.END_CRYSTAL);
		builder(EnchancementEntityTypeTags.BYPASSES_DECREASING_DAMAGE)
				.add(EnchancementEntityTypeIds.AMETHYST_SHARD)
				.add(EnchancementEntityTypeIds.ICE_SHARD)
				.add(EnchancementEntityTypeIds.TORCH);
		builder(EnchancementEntityTypeTags.CANNOT_BURY)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.add(EntityTypeIds.CREAKING)
				.add(EntityTypeIds.ELDER_GUARDIAN)
				.add(EntityTypeIds.WARDEN)
				.add(EntityTypeIds.VEX);
		builder(EnchancementEntityTypeTags.CANNOT_DISARM)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES);
		builder(EnchancementEntityTypeTags.CANNOT_FLUID_WALK)
				.forceAddTag(EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR);
		builder(EnchancementEntityTypeTags.CANNOT_FREEZE)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.forceAddTag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES);
		builder(EnchancementEntityTypeTags.VEIL_IMMUNE)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.add(EntityTypeIds.ELDER_GUARDIAN)
				.add(EntityTypeIds.WARDEN);
	}
}
