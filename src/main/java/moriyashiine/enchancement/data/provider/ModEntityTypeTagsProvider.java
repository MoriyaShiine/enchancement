/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.init.ModEntityTypes;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends FabricTagsProvider.EntityTypeTagsProvider {
	public ModEntityTypeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		valueLookupBuilder(ModEntityTypeTags.BRIMSTONE_HITTABLE)
				.forceAddTag(ConventionalEntityTypeTags.BOATS)
				.forceAddTag(ConventionalEntityTypeTags.MINECARTS)
				.add(EntityType.END_CRYSTAL);
		valueLookupBuilder(ModEntityTypeTags.BYPASSES_DECREASING_DAMAGE)
				.add(ModEntityTypes.AMETHYST_SHARD)
				.add(ModEntityTypes.ICE_SHARD)
				.add(ModEntityTypes.TORCH);
		valueLookupBuilder(ModEntityTypeTags.CANNOT_BURY)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.add(EntityType.CREAKING)
				.add(EntityType.ELDER_GUARDIAN)
				.add(EntityType.WARDEN)
				.add(EntityType.VEX);
		valueLookupBuilder(ModEntityTypeTags.CANNOT_DISARM)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES);
		valueLookupBuilder(ModEntityTypeTags.CANNOT_FREEZE)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.forceAddTag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES);
		valueLookupBuilder(ModEntityTypeTags.VEIL_IMMUNE)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.add(EntityType.ELDER_GUARDIAN)
				.add(EntityType.WARDEN);
	}
}
